package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "codeForgotPass")
@Data
public class CodeForgotPass {
    @Id
    @Indexed(name = "username", type = "string")
    private String username;
    @Indexed(name = "code", type = "string")
    private String code;
    @Indexed(name = "expireDate", type = "string")
    private Long expireDate;
}
