package com.bka.gpstracker.service;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.CompletedDeliveryEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.event.InProgressDeliveryEvent;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.repository.DeliveryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryDriverService {
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<DeliveryInfo> getByDriverUsername(String driverUsername) {
        return deliveryInfoRepository.getAllByDriverUsername(driverUsername);
    }

    public DeliveryInfo driverStartDelivery(String id) {
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        if (!deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.NEW))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);
        deliveryInfo.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);
        DeliveryInfo result = deliveryInfoRepository.save(deliveryInfo);
        applicationEventPublisher.publishEvent(new InProgressDeliveryEvent(result));
        return result;
    }

    public DeliveryInfo driverCompletedDelivery(String id) {
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        if (!deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.IN_PROGRESS))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);
        deliveryInfo.setDeliveryStatus(DeliveryStatus.COMPLETED);
        DeliveryInfo result = deliveryInfoRepository.save(deliveryInfo);
        applicationEventPublisher.publishEvent(new CompletedDeliveryEvent(result));
        return result;
    }
}
