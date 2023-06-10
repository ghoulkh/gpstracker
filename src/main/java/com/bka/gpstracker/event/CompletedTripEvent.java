package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.context.ApplicationEvent;

public class CompletedTripEvent extends ApplicationEvent {
    public CompletedTripEvent(Trip trip) {
        super(trip);
    }
}

