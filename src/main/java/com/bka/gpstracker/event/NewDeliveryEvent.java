package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.context.ApplicationEvent;

public class NewDeliveryEvent extends ApplicationEvent {

    public NewDeliveryEvent(DeliveryInfo deliveryInfo) {
        super(deliveryInfo);
    }
}
