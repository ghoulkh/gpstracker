package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.CurrentPosition;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface CurrentPositionRepository extends SolrCrudRepository<CurrentPosition, String> {
    @Query("username:?0")
    CurrentPosition getByUsername(String username);
}
