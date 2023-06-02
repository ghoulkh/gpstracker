package com.bka.gpstracker.model.response;

import lombok.Data;

@Data
public class Response {
    private String status;
    public Response() {
        this.status = "OK";
    }
}
