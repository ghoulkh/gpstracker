package com.bka.gpstracker.event;

import com.bka.gpstracker.entity.CheckIn;
import org.springframework.context.ApplicationEvent;

public class NewCheckInEvent extends ApplicationEvent {
    public NewCheckInEvent(CheckIn checkIn) {
        super(checkIn);
    }
}
