package com.bka.gpstracker.event;

import com.bka.gpstracker.entity.PositionLog;
import org.springframework.context.ApplicationEvent;

public class NewPositionEvent extends ApplicationEvent {

    public NewPositionEvent(PositionLog positionLog) {
        super(positionLog);
    }
}
