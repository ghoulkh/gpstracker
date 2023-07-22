package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.context.ApplicationEvent;

public class DriverCancelDeliveryEvent extends ApplicationEvent {
    public DriverCancelDeliveryEvent(DeliveryInfo deliveryInfo) {
        super(deliveryInfo);
    }
}
