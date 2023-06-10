package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.context.ApplicationEvent;

public class DriverAcceptTripEvent extends ApplicationEvent {
    public DriverAcceptTripEvent(Trip trip) {
        super(trip);
    }
}
