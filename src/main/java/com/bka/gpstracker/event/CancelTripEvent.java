package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.context.ApplicationEvent;

public class CancelTripEvent extends ApplicationEvent {
    public CancelTripEvent(Trip trip) {
        super(trip);
    }
}
