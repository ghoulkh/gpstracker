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

    @Query("createdBy:?0")
    List<DeliveryInfo> getAllByCreatedBy(String createdBy, Pageable pageable);

    @Query("?0")
    List<DeliveryInfo> query(String query);

    @Query("driverUsername:?0")
    List<DeliveryInfo> getAllByDriverUsername(String driverUsername);

    @Query("id:?0 AND (emailReceiver:?1 OR phoneNumberReceiver:?1 OR senderEmail:?1)")
    Optional<DeliveryInfo> findByIdAndAndEmailReceiver(String deliveryId, String emailOrPhone);
}
