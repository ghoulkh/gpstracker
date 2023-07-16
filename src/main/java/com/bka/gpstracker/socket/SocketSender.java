package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.CheckIn;
import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.socket.message.SocketCode;
import com.bka.gpstracker.socket.message.SocketMessage;
import com.bka.gpstracker.socket.message.SocketMessageContainer;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
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
    private static final String ADMIN_TOPIC = "/admin/trips";
    private static final String USER_PREFIX = "/user/";
    private static final String ADMIN_DELIVERY_TOPIC = "/admin/deliveries";
    private static final String CHECK_IN_TOPIC = "/checkin/realtime";


    public void sendCommentToClient(PositionLog positionLog) {
        simpMessagingTemplate.convertAndSend(PREFIX_TOPIC + positionLog.getRfid(), positionLog);
    }

    public void sendEventTripInProgressToUser(Trip trip, String username) {
        SocketMessageContainer container = new SocketMessageContainer(SocketMessageContainer.Type.TRIP_IN_PROGRESS, trip);
        simpMessagingTemplate.convertAndSend(USER_PREFIX + username, container);
    }
    public void sendEventTripInProgressToDriver(Trip trip, String username) {
        SocketMessageContainer container = new SocketMessageContainer(SocketMessageContainer.Type.TRIP_IN_PROGRESS, trip);
        simpMessagingTemplate.convertAndSend(DRIVER_PREFIX + username, container);
    }

    public void sendToDriver(Trip trip, String username) {
        SocketMessageContainer socketMessageContainer = new SocketMessageContainer(SocketMessageContainer.Type.NEW_TRIP, trip);
        simpMessagingTemplate.convertAndSend(DRIVER_PREFIX + username, socketMessageContainer);
    }

    public void sendToAdmin(Trip trip) {
        SocketMessageContainer socketMessageContainer = new SocketMessageContainer(SocketMessageContainer.Type.NEW_TRIP, trip);
        simpMessagingTemplate.convertAndSend(ADMIN_TOPIC, socketMessageContainer);
    }

    public void sendCancelTripToAdmin(Trip trip) {
        SocketMessageContainer socketMessageContainer = new SocketMessageContainer(SocketMessageContainer.Type.CANCEL_TRIP, trip);
        simpMessagingTemplate.convertAndSend(ADMIN_TOPIC, socketMessageContainer);
    }

    public void sendCheckInToUser(CheckIn checkIn) {
        simpMessagingTemplate.convertAndSend(CHECK_IN_TOPIC, checkIn);
    }

    public void sendWarningToDriver(String username) {
        SocketMessageContainer socketMessageContainer = new SocketMessageContainer(SocketMessageContainer.Type.WARNING_MESSAGE, new SocketMessage(SocketCode.WARNING_MESSAGE));
        simpMessagingTemplate.convertAndSend(DRIVER_PREFIX + username, socketMessageContainer);
    }

    public void sendDeliveryToDriver(DeliveryInfo deliveryInfo, SocketMessageContainer.Type type) {
        simpMessagingTemplate.convertAndSend(DRIVER_PREFIX, new SocketMessageContainer(type, deliveryInfo));
    }

    public void sendDeliveryToAdmin(DeliveryInfo deliveryInfo, SocketMessageContainer.Type type) {
        simpMessagingTemplate.convertAndSend(ADMIN_DELIVERY_TOPIC, new SocketMessageContainer(type, deliveryInfo));
    }

}
