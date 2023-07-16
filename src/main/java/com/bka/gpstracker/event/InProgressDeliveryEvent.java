package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.context.ApplicationEvent;

public class InProgressDeliveryEvent extends ApplicationEvent {
    public InProgressDeliveryEvent(DeliveryInfo deliveryInfo) {
        super(deliveryInfo);
    }
}
