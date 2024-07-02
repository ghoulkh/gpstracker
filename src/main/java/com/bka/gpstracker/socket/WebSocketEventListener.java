package com.bka.gpstracker.socket;

import com.bka.gpstracker.config.JwtToken;
import com.bka.gpstracker.event.InactiveDriverEvent;
import com.bka.gpstracker.event.NewDriverActive;
import com.bka.gpstracker.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private MessageChannel clientOutboundChannel;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private DriverService driverService;

    private static final String INVALID_TOKEN = "INVALID_TOKEN";
    private static final String UNAUTHORISED = "Tài xế chưa được gán thông tin xe hoặc đang trong 1 chuyến xe!";
    private static final String INVALID_POSITION = "yêu cầu cập nhật vị trí!";
    private static final String INVALID_USERNAME = "username không đúng!";

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        if (destination != null && destination.startsWith("/driver")) {
            String username = driverService.removeActiveDriver(sha.getSessionId());
            applicationEventPublisher.publishEvent(new InactiveDriverEvent(username));
            logger.info("Driver disconnect socket with destination: {} and id: {}", destination, sha.getSessionId());
        } else {
            logger.info("Client disconnect socket with destination: {} and id: {}", destination, sha.getSessionId());
        }
    }

    @EventListener
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
//        String token = sha.getFirstNativeHeader("token");
//        diverCanDrive(token, destination, sha.getSessionId());
        logger.info("Event subscriber with destination: {}", destination);
    }

    private void diverCanDrive(String token, String destination, String clientId) {
        if (!destination.startsWith("/driver")) return;
        String username = jwtToken.validateToken(token);
        if (!driverService.canDrive(username)) {
            sendMsg(clientId, UNAUTHORISED, StompCommand.ERROR);
            return;
        }
        if (!driverService.isExistPosition(username)) {
            sendMsg(clientId, INVALID_POSITION, StompCommand.ERROR);
            return;
        }
        if (!destination.contains(username)) {
            sendMsg(clientId, INVALID_USERNAME, StompCommand.ERROR);
        }
        driverService.addActiveDriver(username, clientId);
        applicationEventPublisher.publishEvent(new NewDriverActive(username));
        logger.info("driver active with destination: {}, clientId: {}, username: {}", destination, clientId, username);
    }



    private void sendMsg(String sessionId, String message, StompCommand command) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(command);
        headerAccessor.setMessage(message);
        headerAccessor.setSessionId(sessionId);
        clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }
}
