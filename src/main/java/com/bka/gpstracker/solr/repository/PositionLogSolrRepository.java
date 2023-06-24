package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.Position;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface PositionLogSolrRepository extends SolrCrudRepository<Position, String> {
    @Query("?0")
    List<Position> getByQuery(String query, Pageable pageable );
}
