package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.Trip;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface TripRepository extends SolrCrudRepository<Trip, String> {
    @Query("username:?0 AND (status:NEW OR status:IN_PROGRESS)")
    List<Trip> findAllByCreatedByAndStatusNEWOrInProgress(String username);
}
