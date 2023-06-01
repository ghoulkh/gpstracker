package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.UserInfo;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface UserInfoRepository extends SolrCrudRepository<UserInfo, String> {

    @Query("username:?0")
    UserInfo getByUsername(String username);
}
