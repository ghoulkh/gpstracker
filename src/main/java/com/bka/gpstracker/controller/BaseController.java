package com.bka.gpstracker.controller;

import com.bka.gpstracker.model.response.Response;
import org.springframework.http.ResponseEntity;

public class BaseController {
    protected ResponseEntity<Response> makeResponseOk() {
        return ResponseEntity.ok(new Response());
    }
}
