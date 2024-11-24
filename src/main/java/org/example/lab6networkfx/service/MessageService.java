package org.example.lab6networkfx.service;

import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.domain.messages.ReplyMessage;
import org.example.lab6networkfx.exceptions.ServiceException;
import org.example.lab6networkfx.repository.Repository;
import org.example.lab6networkfx.utils.events.EventType;
import org.example.lab6networkfx.utils.events.NetworkEvent;
import org.example.lab6networkfx.utils.observer.Observable;
import org.example.lab6networkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MessageService extends MessageSrv implements Observable<NetworkEvent> {
    private final Repository messageRepo;
    private final Repository replyRepo;
    private List<Observer<NetworkEvent>> observers = new ArrayList<>();

    public MessageService(Repository userRepo, Repository friendshipRepo, Repository messageRepo, Repository replyRepo) {
        super(userRepo, friendshipRepo);
        this.messageRepo = messageRepo;
        this.replyRepo = replyRepo;
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepo.findAll();
    }

    public List<ReplyMessage> getAllReplies() {
        return (List<ReplyMessage>) replyRepo.findAll();
    }

    public List<Message> getMessagesForUser(String username) {
        User user = findUsername(username);
        if (user == null) {
            return null;
        }

        List<Message> allMessages = getAllMessages();
        List<Message> messages = StreamSupport.stream(allMessages.spliterator(), false)
                .filter(m -> {
                    return m.getTo().contains(user);
                }).collect(Collectors.toList());

        return messages;
    }

    public List<Message> getMessagesFromU1toU2(String username1, String username2) {
        User user2 = findUsername(username2);
        List<Message> messages = getMessagesForUser(username1);
        return StreamSupport.stream(messages.spliterator(), false)
                .filter(m -> m.getFrom().getUsername().equals(username2))
                .collect(Collectors.toList());
    }

    public List<ReplyMessage> getRepliesOfUser(String username) {
        User user = findUsername(username);
        if (user == null) {
            return null;
        }

        List<ReplyMessage> allReplies = getAllReplies();
        List<ReplyMessage> replies = StreamSupport.stream(allReplies.spliterator(), false)
                .filter(m -> {
                    return m.getReceiver().equals(user);
                }).collect(Collectors.toList());

        return replies;
    }

    public List<ReplyMessage> getRepliesOfUser1ToUser2(String username1, String username2) {
        List<ReplyMessage> replyMessages=getRepliesOfUser(username1);
        return StreamSupport.stream(replyMessages.spliterator(),false).
                filter(replyMessage -> {
                    return replyMessage.getReplyFor().getFrom().getUsername().equals(username2)
                            &&replyMessage.getReply()!=null;
                }).collect(Collectors.toList());
    }

    public List<String> getConversation(String username1, String username2) {
        User user1 = findUsername(username1);
        User user2 = findUsername(username2);

        if (user1 == null || user2 == null) {
            throw new ServiceException("User not found");
        }

        List<Message> user1MessagesFromUser2 = getMessagesFromU1toU2(username1, username2);
        List<ReplyMessage> user1RepliesToUser2 = getRepliesOfUser1ToUser2(username1, username2);
        List<Message> user2MessagesFromUser1 = getMessagesFromU1toU2(username2, username1);
        List<ReplyMessage> user2RepliesToUser1 = getRepliesOfUser1ToUser2(username2, username1);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(user1MessagesFromUser2);
        allMessages.addAll(user2MessagesFromUser1);
        Collections.sort(allMessages, Comparator.comparing(Message::getDate));

        List<ReplyMessage> allReplies = new ArrayList<>();
        allReplies.addAll(user1RepliesToUser2);
        allReplies.addAll(user2RepliesToUser1);
        Collections.sort(allReplies, Comparator.comparing(ReplyMessage::getDate));

        List<Tuple<String, LocalDateTime>> messages = new ArrayList<>();
        int i = 0;
        for (Message message : allMessages) {
            messages.add(new Tuple(message.getFrom().getUsername() + ": " + message.getMessage(), message.getDate()));
        }

        for (ReplyMessage reply : allReplies) {
            if (reply.getReplyFor() != null) {

                while( i < messages.size() && reply.getDate().compareTo(messages.get(i).getSecond()) > 0) {
                    i++;
                }

                String mes = "\t\t" + reply.getReceiver().getUsername() + " replied to " + reply.getReplyFor().getFrom().getUsername() + ": " + reply.getReply();
                messages.add(i, new Tuple(mes, reply.getDate()));
                i++;
            }
        }

        List<String> convo=StreamSupport.stream(messages.spliterator(),false).map(tuple->
        {return tuple.getFirst();}).collect(Collectors.toList());
        return convo;
    }

    private List<User> getUsers(String users) {
        List<User> userList = new ArrayList<>();
        String[] usernames = users.split(" ");

        for (String username: usernames) {
            User user = findUsername(username);
            if (user == null) {
                throw new ServiceException("User not found");
            }
            userList.add(user);
        }

        return userList;
    }

    public void addMessage(String from, String to, String text) {
        User userFrom = findUsername(from);
        if (userFrom == null) {
            throw new ServiceException("User not found");
        }

        List<User> users = getUsers(to);
        Message message = new Message(userFrom, text, LocalDateTime.now(), users);
        if (messageRepo.save(message).isEmpty()) {
            Message finalMessage = message;
            Iterable<Message> allMessages = messageRepo.findAll();
            List<Message> mesL= StreamSupport.stream(allMessages.spliterator(),false)
                    .filter(mes-> mes.getFrom().getUsername().equals(finalMessage.getFrom().getUsername()))
                    .filter(mes-> mes.getTo().get(0).getFirstName() == null && mes.getTo().get(0).getUsername()==null).
                    collect(Collectors.toList());
            if(mesL.size()!=0)
                message=mesL.get(0);
        }

        for (User user : users) {
            ReplyMessage replyMessage = new ReplyMessage(message, null, user, LocalDateTime.now());
            replyRepo.save(replyMessage);
        }

        notifyObservers(new NetworkEvent(EventType.ADDMSG, message));
    }

    public void deleteMessage(Integer id) {
        Message message = (Message) messageRepo.findOne(id).orElse(null);
        messageRepo.delete(id);

        notifyObservers(new NetworkEvent(EventType.DELETEMSG, message));
    }

    public void sendReply(Integer idMessage, String reply, String usernameReceiver) {
        User receiver = findUsername(usernameReceiver);

        if (receiver == null) {
            throw new ServiceException("User not found");
        }

        Tuple<Integer, Integer> id = new Tuple<>(idMessage, receiver.getId());
        Message message =(Message) messageRepo.findOne(idMessage).orElse(null);
        ReplyMessage replyMessage = new ReplyMessage(message, reply, receiver, LocalDateTime.now());
        replyRepo.update(replyMessage);

        notifyObservers(new NetworkEvent(EventType.ADDREPLY, replyMessage));
    }

    public void deleteReply(Integer idMessage, String usernameReceiver) {
        User receiver = findUsername(usernameReceiver);
        Tuple<Integer, Integer> id = new Tuple<>(idMessage, receiver.getId());
        ReplyMessage replyMessage = (ReplyMessage) replyRepo.findOne(id).orElse(null);
        replyRepo.delete(id);

        notifyObservers(new NetworkEvent(EventType.DELETEREPLY, replyMessage));
    }

    @Override
    public void addObserver(Observer<NetworkEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<NetworkEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(NetworkEvent networkEvent) {
        observers.stream().forEach(x -> x.update(networkEvent));
    }
}
