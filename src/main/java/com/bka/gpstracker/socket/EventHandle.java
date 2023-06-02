package com.bka.gpstracker.socket;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.NewPositionEvent;
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
        log.info("Handle new position with rfid {} done!!", positionLog.getRfid());
    }




}
