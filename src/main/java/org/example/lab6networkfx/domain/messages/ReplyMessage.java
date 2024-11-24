package org.example.lab6networkfx.domain.messages;

import org.example.lab6networkfx.domain.Entity;
import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReplyMessage extends Entity<Tuple<Integer, Integer>> {
    private Message replyFor;
    private String reply;
    private User receiver;
    private LocalDateTime date;

    public ReplyMessage(Message replyFor, String reply, User receiver, LocalDateTime date) {
        this.replyFor = replyFor;
        this.reply = reply;
        this.receiver = receiver;
        this.date = date;
        this.setId(new Tuple<>(replyFor.getId(), receiver.getId()));
    }

    public Message getReplyFor() {
        return replyFor;
    }

    public void setReplyFor(Message replyFor) {
        this.replyFor = replyFor;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyMessage that = (ReplyMessage) o;
        return Objects.equals(getReplyFor(), that.getReplyFor()) && Objects.equals(getReply(), that.getReply()) && Objects.equals(getReceiver(), that.getReceiver()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReplyFor(), getReply(), getReceiver(), getDate());
    }
}
