package com.bka.gpstracker.solr.repository;

import com.bka.gpstracker.solr.entity.CodeForgotPass;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface CodeForgotPassRepository extends SolrCrudRepository<CodeForgotPass, String> {
}
