package com.bka.gpstracker.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ACCESS_DENIED("CD-010", "Xin lỗi, bạn không có quyền truy cập!", HttpStatus.FORBIDDEN),
    BAD_REQUEST("CD-010", "Yêu cầu của bạn chưa đúng!!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("CD-11", "UserInfo not found!", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("UNAUTHORIZED", "Login session expired!", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("INVALID_TOKEN", "invalid token!", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER("INTERNAL-SERVER", "internal server!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USERNAME_OR_PASSWORD("APP-22", "Invalid username or password!", HttpStatus.FORBIDDEN),
    CONFLICT_USERNAME("APP-20", "Conflict username!", HttpStatus.CONFLICT),
    FORBIDDEN("APP-29", "you do not have permission!", HttpStatus.FORBIDDEN),
    UNREGISTERED_RFID("APP-30", "Bạn chưa đăng ký rfid, liên hệ với admin để đăng ký rfid!", HttpStatus.NOT_FOUND),
    ENTERPRISE_EXIST("APP-31", "enterprise existed!", HttpStatus.CONFLICT),
    ENTERPRISE_NOT_FOUND("APP-32", "enterprise not found!", HttpStatus.NOT_FOUND),
    ENTERPRISE_CODE_INVALID("APP--34", "enterpriseCode invalid!", HttpStatus.BAD_REQUEST),
    ROLE_INVALID("APP-35", "Tài khoản này chưa được gán với danh nghiệp nào, yêu cầu user cập nhật lại thông tin!", HttpStatus.BAD_REQUEST),
    ROLE_IS_EXIST("APP-36", "User đã được gán role này!", HttpStatus.BAD_REQUEST),
    ADD_ROLE_PERMISSION_DENIED("APP-37", "ADMIN ENTERPRISE không thể phân quyền cho tài khoản của danh nghiệp khác!", HttpStatus.BAD_REQUEST),
    ADD_ROLE_ADMIN_PERMISSION_DENIED("APP-38", "ADMIN ENTERPRISE không thể phân quyền cho ADMIN", HttpStatus.BAD_REQUEST),
    SET_CAR_INFO_PERMISSION_DENIED("APP-39", "ADMIN ENTERPRISE không thể set thông tin xe của doanh nghiệp khác!", HttpStatus.BAD_REQUEST),
    CAR_INFO_NOT_FOUND("APP-40", "CarInfo not found!", HttpStatus.NOT_FOUND);



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


