package org.example.lab6networkfx.service;

import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.repository.Repository;
import org.example.lab6networkfx.utils.events.EventType;
import org.example.lab6networkfx.utils.events.NetworkEvent;
import org.example.lab6networkfx.utils.observer.Observable;
import org.example.lab6networkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService extends MessageSrv implements Observable<NetworkEvent> {
    private final Repository messageRepo;
    private List<Observer<NetworkEvent>> observers = new ArrayList<>();

    public MessageService(Repository userRepo, Repository friendshipRepo, Repository messageRepo) {
        super(userRepo, friendshipRepo);
        this.messageRepo = messageRepo;
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepo.findAll();
    }

    public List<Message> getMessagesForUser(User user) {
        return getAllMessages().stream()
                .filter(x -> x.getTo().equals(user))
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesFromUser(User user) {
        return getAllMessages().stream()
                .filter(x -> x.getFrom().equals(user))
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesBetweenUsers(User user1, User user2) {
        List<Message> conversation =  getAllMessages().stream()
                .filter(x -> x.getFrom().equals(user1) && x.getTo().equals(user2) ||
                        x.getFrom().equals(user2) && x.getTo().equals(user1))
                .collect(Collectors.toList());

        conversation.sort(Comparator.comparing(Message::getDate));
        return conversation;
    }

    public void sendMessage(User from, User to, String message, Integer replyingTo) {
        Message msg = new Message(message, LocalDateTime.now(), replyingTo, to, from);
        messageRepo.save(msg);
        notifyObservers(new NetworkEvent(EventType.ADDMSG, msg));
    }

    public void deleteMessage(Message message) {
        messageRepo.delete(message.getId());
        notifyObservers(new NetworkEvent(EventType.DELETEMSG, message));
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
