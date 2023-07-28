package com.bka.gpstracker.service;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.NewDeliveryEvent;
import com.bka.gpstracker.event.UpdateDeliveryEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.StatusHistory;
import com.bka.gpstracker.model.request.NewDeliveryRequest;
import com.bka.gpstracker.model.request.UpdateDeliveryRequest;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.repository.DeliveryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DeliveryAdminService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private AddressService addressService;

    public DeliveryInfo registrationDelivery(NewDeliveryRequest request, String currentUsername) {
        userService.findByUsername(request.getDriverUsername()).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DRIVER_USERNAME_INVALID));

        List<CarInfo> carInfos = carInfoService.getAllByUsername(request.getDriverUsername());
        if (carInfos.isEmpty()) throw new TrackerAppException(ErrorCode.DRIVER_IS_INACTIVE);
        CarInfo carInfo = carInfos.get(0);
        if (!carInfo.getActiveAreas().isEmpty()) {
            String districtInAddress = addressService.getDistrictInAddress(request.getToAddress());
            if (!carInfo.getActiveAreas().contains(districtInAddress))
                throw new TrackerAppException(ErrorCode.INVALID_DRIVER_AREA);
        }

        DeliveryInfo result = deliveryInfoRepository.save(request.toDeliveryInfo(currentUsername));
        applicationEventPublisher.publishEvent(new NewDeliveryEvent(result));
        return result;
    }

    public List<DeliveryInfo> getAllDeliveryCanceled(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return deliveryInfoRepository.getAllByStatusCanceled(pageable);
    }

    public DeliveryInfo updateDelivery(UpdateDeliveryRequest updateDeliveryRequest, String id) {
        DeliveryInfo deliveryToUpdate = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));

        if (updateDeliveryRequest.getDriverUsername() != null
                && !updateDeliveryRequest.getDriverUsername().equals(deliveryToUpdate.getDriverUsername())
                && deliveryToUpdate.getDeliveryStatus().equals(DeliveryStatus.CANCELED)) {
            addStatusHistory(deliveryToUpdate, DeliveryStatus.NEW_DRIVER);
        }
        updateDeliveryRequest.updateDelivery(deliveryToUpdate);
        if (updateDeliveryRequest.getDriverUsername() == null) {
            userService.findByUsername(updateDeliveryRequest.getDriverUsername()).orElseThrow(() ->
                    new TrackerAppException(ErrorCode.DRIVER_USERNAME_INVALID));
        }

        DeliveryInfo result = deliveryInfoRepository.save(deliveryToUpdate);
        applicationEventPublisher.publishEvent(new UpdateDeliveryEvent(deliveryToUpdate, result));
        return result;
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

    public List<DeliveryInfo> getDeliveries(String createdBy, String driver, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return deliveryInfoRepository.getAllByCreatedByOrDriverUsername(createdBy, driver, pageable);
    }

    public List<DeliveryInfo> search(String keyword) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("searchData:(");
        String[] wordsInKeyword = keyword.split(" ");
        for (int i = 0; i < wordsInKeyword.length; i ++) {
            queryBuilder.append("*")
                    .append(wordsInKeyword[i])
                    .append("* ");
        }
        queryBuilder.append(")");

        return deliveryInfoRepository.query(queryBuilder.toString());
    }
}
