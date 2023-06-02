package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.context.ApplicationEvent;

public class NewTripEvent extends ApplicationEvent {
    public NewTripEvent(Trip trip) {
        super(trip);
    }
}
