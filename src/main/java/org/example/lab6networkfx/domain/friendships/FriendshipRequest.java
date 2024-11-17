package org.example.lab6networkfx.domain.friendships;

import org.example.lab6networkfx.domain.Entity;
import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FriendshipRequest extends Entity<Tuple<Integer, Integer>> {
    private User user1;
    private User user2;
    private FriendshipStatus status;
    private LocalDateTime date;

    public FriendshipRequest(User user1, User user2, FriendshipStatus status, LocalDateTime date) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
        this.status = status;

        Integer id1 = user1.getId();
        Integer id2 = user2.getId();
        Tuple<Integer, Integer> id = new Tuple<>(id1, id2);
        this.setId(id);
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
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
        FriendshipRequest that = (FriendshipRequest) o;
        return Objects.equals(getUser1(), that.getUser1()) && Objects.equals(getUser2(), that.getUser2()) && getStatus() == that.getStatus() && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser1(), getUser2(), getStatus(), getDate());
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
