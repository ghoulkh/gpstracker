package com.bka.gpstracker.model.request;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.Utils;
import lombok.Data;



@Data
public class UpdateDeliveryRequest {
    private String fromAddress;
    private String toAddress;
    private String driverUsername;
    private String fullNameReceiver;
    private String phoneNumberReceiver;
    private String emailReceiver;
    private String senderFullName;
    private String senderEmail;

    public void updateDelivery(DeliveryInfo deliveryInfo) {
        Utils.copyPropertiesNotNull(this, deliveryInfo);
        deliveryInfo.setSearchData(Utils.geneDataSearchDelivery(deliveryInfo));
    }
}
