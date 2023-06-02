package com.bka.gpstracker.event;

import org.springframework.context.ApplicationEvent;

public class InactiveDriverEvent extends ApplicationEvent {
    public InactiveDriverEvent(String username) {
        super(username);
    }
}
