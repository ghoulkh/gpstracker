package com.bka.gpstracker.service;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.CompletedDeliveryEvent;
import com.bka.gpstracker.event.DriverCancelDeliveryEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.event.InProgressDeliveryEvent;
import com.bka.gpstracker.model.StatusHistory;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.repository.DeliveryInfoRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DeliveryDriverService {
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<DeliveryInfo> getByDriverUsernameAndStatus(String driverUsername, String status, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (DeliveryStatus.NEW.toString().equals(status)) {
            status = "(" + DeliveryStatus.NEW + " " + DeliveryStatus.NEW_DRIVER + ")";
        }
        return deliveryInfoRepository.getAllByDriverUsernameAndStatus(driverUsername, status, pageable);
    }

    public DeliveryInfo driverStartDelivery(String id) {
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        if (!deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.NEW) && !deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.NEW_DRIVER))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);

        canChangeStatus(deliveryInfo);

        addStatusHistory(deliveryInfo, DeliveryStatus.IN_PROGRESS);
        deliveryInfo.setLastUpdatedAt(System.currentTimeMillis());
        DeliveryInfo result = deliveryInfoRepository.save(deliveryInfo);
        applicationEventPublisher.publishEvent(new InProgressDeliveryEvent(result));
        return result;
    }

    private void canChangeStatus(DeliveryInfo deliveryInfo) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        if (!deliveryInfo.getDriverUsername().equals(currentUsername))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);
    }

    private void addStatusHistory(DeliveryInfo deliveryInfo, DeliveryStatus statusToAdd) {
        List<StatusHistory> statusHistories = deliveryInfo.getStatusHistories();
        if (statusHistories == null)
            statusHistories = new LinkedList<>();
        StatusHistory statusHistoryToAdd = new StatusHistory();
        statusHistoryToAdd.setCreatedAt(System.currentTimeMillis());
        statusHistoryToAdd.setDeliveryStatus(statusToAdd);
        statusHistories.add(statusHistoryToAdd);

        deliveryInfo.setStatusHistories(statusHistories);
        deliveryInfo.setDeliveryStatus(statusToAdd);
    }

    public DeliveryInfo driverCompletedDelivery(String id) {
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        if (!deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.IN_PROGRESS))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);

        canChangeStatus(deliveryInfo);

        addStatusHistory(deliveryInfo, DeliveryStatus.COMPLETED);
        deliveryInfo.setLastUpdatedAt(System.currentTimeMillis());
        DeliveryInfo result = deliveryInfoRepository.save(deliveryInfo);
        applicationEventPublisher.publishEvent(new CompletedDeliveryEvent(result));
        return result;
    }

    public DeliveryInfo driverCancelDelivery(String id) {
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        if (deliveryInfo.getDeliveryStatus().equals(DeliveryStatus.COMPLETED))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);
        addStatusHistory(deliveryInfo, DeliveryStatus.CANCELED);

        canChangeStatus(deliveryInfo);

        DeliveryInfo result = deliveryInfoRepository.save(deliveryInfo);
        applicationEventPublisher.publishEvent(new DriverCancelDeliveryEvent(result));
        return result;
    }
}
