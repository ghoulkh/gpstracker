package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.DeliveryInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryInfoRepository extends SolrCrudRepository<DeliveryInfo, String> {

    @Override
    @Query("id:?0")
    Optional<DeliveryInfo> findById(String s);

    @Query("createdBy:?0 AND driverUsername:?1")
    List<DeliveryInfo> getAllByCreatedByOrDriverUsername(String createdBy, String driverUsername, Pageable pageable);

    @Query("status:CANCELED")
    List<DeliveryInfo> getAllByStatusCanceled(Pageable pageable);

    @Query("?0")
    List<DeliveryInfo> query(String query);

    @Query("driverUsername:?0 AND status:?1")
    List<DeliveryInfo> getAllByDriverUsernameAndStatus(String driverUsername, String status, Pageable pageable);

    @Query("id:?0 AND (emailReceiver:?1 OR phoneNumberReceiver:?1 OR senderEmail:?1)")
    Optional<DeliveryInfo> findByIdAndAndEmailReceiver(String deliveryId, String emailOrPhone);
}
