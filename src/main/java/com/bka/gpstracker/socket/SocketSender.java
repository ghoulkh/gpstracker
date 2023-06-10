package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketSender {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final String PREFIX_TOPIC = "/rfid/";
    private static final String DRIVER_PREFIX = "/driver/";

    public void sendCommentToClient(PositionLog positionLog) {
        simpMessagingTemplate.convertAndSend(PREFIX_TOPIC + positionLog.getRfid(), positionLog);
    }

    public void sendToDriver(Trip trip, String username) {
        simpMessagingTemplate.convertAndSend(DRIVER_PREFIX + username, trip);
    }

}
