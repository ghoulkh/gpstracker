package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.PositionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketSender {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final String PREFIX_TOPIC = "/rfid/";

    public void sendCommentToClient(PositionLog positionLog) {
        simpMessagingTemplate.convertAndSend(PREFIX_TOPIC + positionLog.getRfid(), positionLog);
    }

}
