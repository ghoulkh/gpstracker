package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.context.ApplicationEvent;

public class CompletedDeliveryEvent extends ApplicationEvent {

    public CompletedDeliveryEvent(DeliveryInfo deliveryInfo) {
        super(deliveryInfo);
    }
}
