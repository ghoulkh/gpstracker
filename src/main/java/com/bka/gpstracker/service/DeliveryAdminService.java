package com.bka.gpstracker.service;

import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.NewDeliveryEvent;
import com.bka.gpstracker.event.UpdateDeliveryEvent;
import com.bka.gpstracker.exception.TrackerAppException;
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

import java.util.List;

@Service
public class AdminDeliveryService {

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public DeliveryInfo registrationDelivery(NewDeliveryRequest request) {
        DeliveryInfo result = deliveryInfoRepository.save(request.toDeliveryInfo());
        applicationEventPublisher.publishEvent(new NewDeliveryEvent(result));
        return result;
    }

    public DeliveryInfo updateDelivery(UpdateDeliveryRequest updateDeliveryRequest, String id) {
        DeliveryInfo deliveryToUpdate = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));
        updateDeliveryRequest.updateDelivery(deliveryToUpdate);

        DeliveryInfo result = deliveryInfoRepository.save(deliveryToUpdate);
        applicationEventPublisher.publishEvent(new UpdateDeliveryEvent(deliveryToUpdate, result));
        return result;
    }

    public List<DeliveryInfo> getDeliveries(String createdBy, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (createdBy == null) {
            createdBy = "*";
        }
        return deliveryInfoRepository.getAllByCreatedBy(createdBy);
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
