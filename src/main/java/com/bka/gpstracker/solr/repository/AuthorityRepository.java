package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.Authority;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface AuthorityRepository extends SolrCrudRepository<Authority, String> {

    @Query("username:?0")
    List<Authority> getAllByUsername(String username);
    @Query("role:?0")
    List<Authority> getAllByRole(String role);
}
