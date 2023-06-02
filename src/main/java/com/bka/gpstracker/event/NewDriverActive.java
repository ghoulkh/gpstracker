package com.bka.gpstracker.event;

import org.springframework.context.ApplicationEvent;

public class NewDriverActive extends ApplicationEvent {
    public NewDriverActive(String username) {
        super(username);
    }
}
