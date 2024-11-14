package org.example.lab6networkfx.utils.events;

import org.example.lab6networkfx.domain.Entity;

public class NetworkEvent<E extends Entity> implements Event{
    private EventType type;
    private E data, oldData;

    public NetworkEvent(EventType type, E data) {
        this.type = type;
        this.data = data;
    }

    public NetworkEvent(EventType type, E data, E oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public EventType getType() {
        return type;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }
}
