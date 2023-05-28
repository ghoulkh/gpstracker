package com.bka.gpstracker.socket;

import com.bka.gpstracker.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AuthService authService;

    private static final String INVALID_TOKEN = "INVALID_TOKEN";
    private static final String UNAUTHORISED = "UNAUTHORISED";

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("Handle disconnect");
    }

    @EventListener
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        String rfid = destination.substring(destination.lastIndexOf("/") + 1);
        String token = sha.getFirstNativeHeader("token");
        if (!authService.checkPermissionConnectToSocket(token, rfid)) {
            sendMsg(sha.getSessionId(), UNAUTHORISED, StompCommand.ERROR);
        }
    }

    private void sendMsg(String sessionId, String message, StompCommand command) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(command);
        headerAccessor.setMessage(message);
        headerAccessor.setSessionId(sessionId);
        clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }
}
