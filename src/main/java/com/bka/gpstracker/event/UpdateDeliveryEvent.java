package com.bka.gpstracker.event;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.context.ApplicationEvent;

public class UpdateDeliveryEvent extends ApplicationEvent {

    private DeliveryInfo deliveryInfoOld;
    private DeliveryInfo deliveryInfoNew;

    public UpdateDeliveryEvent(DeliveryInfo deliveryInfoOld, DeliveryInfo deliveryInfoNew) {
        super(deliveryInfoOld);
        this.deliveryInfoNew = deliveryInfoNew;
        this.deliveryInfoOld = deliveryInfoOld;
    }

    public DeliveryInfo getDeliveryInfoNew() {
        return deliveryInfoNew;
    }

    public DeliveryInfo getDeliveryInfoOld() {
        return deliveryInfoOld;
    }
}
