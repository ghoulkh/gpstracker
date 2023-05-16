package com.bka.gpstracker.model;

import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import lombok.Data;

@Data
public class ErrorResponse {
    private String code;
    private String description;

    public ErrorResponse() {
    }

    public ErrorResponse(TrackerAppException e) {
        this.code = e.getCode();
        this.description = e.getDescription();
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.code();
        this.description = errorCode.description();
    }
}

