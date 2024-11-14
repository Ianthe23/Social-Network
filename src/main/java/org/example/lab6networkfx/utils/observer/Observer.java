package org.example.lab6networkfx.utils.observer;

import org.example.lab6networkfx.utils.events.Event;

public interface Observer <E extends Event>{
    void update(E e);
}
