package org.example.lab6networkfx.utils.events;

import org.example.lab6networkfx.domain.Friendship;

public class FriendshipStatusEvent implements Event{
    private FriendshipStatusType type;
    private Friendship friendship;

    public FriendshipStatusEvent(FriendshipStatusType type, Friendship friendship) {
        this.type = type;
        this.friendship = friendship;
    }

    public FriendshipStatusType getType() {
        return type;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setType(FriendshipStatusType type) {
        this.type = type;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }
}
