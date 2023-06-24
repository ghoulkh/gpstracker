package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.CancelTripEvent;
import com.bka.gpstracker.event.NewPositionEvent;
import com.bka.gpstracker.event.NewTripEvent;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.solr.repository.CurrentPositionRepository;
import com.bka.gpstracker.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        log.info("Handle new position with rfid {} done!!", positionLog.getRfid());
    }
    @EventListener
    @Async
    public void handNewTrip(NewTripEvent newTripEvent) {
        Trip trip = (Trip) newTripEvent.getSource();
        socketSender.sendToAdmin(trip);
        log.info("Send event new trip to Admin done!, created by {}", trip.getCreatedBy());
//        List<String> sendToUsers = new ArrayList<>();
//        currentPositionRepository.findAll().forEach(currentPosition -> {
//            if (Utils.isMatch(currentPosition, trip))
//                sendToUsers.add(currentPosition.getUsername());
//        });
//
//        for (String driver : sendToUsers) {
//            socketSender.sendToDriver(trip, driver);
//        }
//        log.info("Send new trip to driver done with usernames: {}", sendToUsers);
    }

    @EventListener
    @Async
    public void handleCancelTrip(CancelTripEvent event) {
        Trip trip = (Trip) event.getSource();
        socketSender.sendCancelTripToAdmin(trip);
        log.info("Send event cancel trip to Admin done!, trip created by {}", trip.getCreatedBy());
    }

}
