package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.context.ApplicationEvent;

public class SetDriverForTripEvent extends ApplicationEvent {
    private Trip trip;
    private String driver;
    public SetDriverForTripEvent(Trip trip, String driver) {
        super(trip);
        this.driver = driver;
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getDriver() {
        return driver;
    }
}
