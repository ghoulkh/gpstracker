package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.*;
import com.bka.gpstracker.solr.entity.Trip;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventHandle {

    @Autowired
    private SocketSender socketSender;

    @EventListener
    @Async
    public void handleNewPosition(NewPositionEvent newPositionEvent) {
        PositionLog positionLog = (PositionLog) newPositionEvent.getSource();
        socketSender.sendCommentToClient(positionLog);
        log.info("Handle new position with rfid {} done!! - position: {}", positionLog.getRfid(), positionLog);
    }

    @EventListener
    @Async
    public void handNewTrip(NewTripEvent newTripEvent) {
        Trip trip = (Trip) newTripEvent.getSource();
        socketSender.sendToAdmin(trip);
        log.info("Send event new trip to Admin done!, created by {}", trip.getCreatedBy());
    }

    @EventListener
    @Async
    public void handleCancelTrip(CancelTripEvent event) {
        Trip trip = (Trip) event.getSource();
        socketSender.sendCancelTripToAdmin(trip);
        log.info("Send event cancel trip to Admin done!, trip created by {}", trip.getCreatedBy());
    }

    @EventListener
    @Async
    public void handleSetDriverForTrip(SetDriverForTripEvent event) {
        socketSender.sendEventTripInProgressToDriver(event.getTrip(), event.getDriver());
        socketSender.sendEventTripInProgressToUser(event.getTrip(), event.getTrip().getCreatedBy());
        log.info("Send event new trip to driver: {} done!", event.getDriver());
    }

}
