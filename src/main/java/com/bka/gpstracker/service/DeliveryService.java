package com.bka.gpstracker.service;

import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.repository.DeliveryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    public DeliveryInfoRepository deliveryInfoRepository;

    public DeliveryInfo getByDeliveryIdAndEmailOrPhone(String deliveryId, String emailOrPhone) {
        return deliveryInfoRepository.findByIdAndAndEmailReceiver(deliveryId, emailOrPhone).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
    }
}
