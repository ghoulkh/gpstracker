package com.bka.gpstracker.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ACCESS_DENIED("CD-010", "Xin lỗi, bạn không có quyền truy cập!", HttpStatus.FORBIDDEN),
    BAD_REQUEST("CD-010", "Yêu cầu của bạn chưa đúng!!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("CD-11", "User not found!", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("UNAUTHORIZED", "Login session expired!", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER("INTERNAL-SERVER", "internal server!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USERNAME_OR_PASSWORD("APP-22", "Invalid username or password!", HttpStatus.FORBIDDEN),
    CONFLICT_USERNAME("APP-20", "Conflict username!", HttpStatus.CONFLICT),
    FORBIDDEN("APP-29", "you do not have permission!", HttpStatus.FORBIDDEN);


    private String code;
    private String description;
    private HttpStatus httpStatus;

    ErrorCode(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }
}


