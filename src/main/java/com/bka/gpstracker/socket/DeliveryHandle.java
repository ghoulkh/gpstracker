package com.bka.gpstracker.socket;

import com.bka.gpstracker.event.CompletedDeliveryEvent;
import com.bka.gpstracker.event.InProgressDeliveryEvent;
import com.bka.gpstracker.event.NewDeliveryEvent;
import com.bka.gpstracker.event.UpdateDeliveryEvent;
import com.bka.gpstracker.service.EmailService;
import com.bka.gpstracker.socket.message.SocketMessageContainer;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DeliveryHandle {

    @Autowired
    private SocketSender socketSender;

    @Autowired
    private EmailService emailService;

    @Async
    @EventListener
    public void handleNewDelivery(NewDeliveryEvent newDeliveryEvent) {
        DeliveryInfo deliveryInfo = (DeliveryInfo) newDeliveryEvent.getSource();
        socketSender.sendDeliveryToDriver(deliveryInfo, SocketMessageContainer.Type.NEW_DELIVERY);
        emailService.sendMailNewDelivery(deliveryInfo.getId(), deliveryInfo.getEmailReceiver());
        emailService.sendMailNewDelivery(deliveryInfo.getId(), deliveryInfo.getSenderEmail());
        log.info("Send new delivery to driver done!");
    }

    @Async
    @EventListener
    public void handleUpdateDelivery(UpdateDeliveryEvent event) {
        DeliveryInfo deliveryInfo = event.getDeliveryInfoNew();
        socketSender.sendDeliveryToDriver(deliveryInfo, SocketMessageContainer.Type.UPDATE_DELIVERY);
        log.info("Send update delivery event to driver done!");
    }

    @Async
    @EventListener
    public void handleInProgressDelivery(InProgressDeliveryEvent event) {
        DeliveryInfo deliveryInfo = (DeliveryInfo) event.getSource();
        socketSender.sendDeliveryToAdmin(deliveryInfo, SocketMessageContainer.Type.DELIVERY_IN_PROGRESS);
        log.info("Send event delivery in progress to admin done!");
    }

    @Async
    @EventListener
    public void handleCompletedDelivery(CompletedDeliveryEvent event) {
        DeliveryInfo deliveryInfo = (DeliveryInfo) event.getSource();
        socketSender.sendDeliveryToAdmin(deliveryInfo, SocketMessageContainer.Type.DELIVERY_COMPLETED);
        log.info("Send event completed delivery to admin done!");
    }

}
