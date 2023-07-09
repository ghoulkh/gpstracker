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
    CAR_INFO_NOT_FOUND("APP-40", "CarInfo not found!", HttpStatus.NOT_FOUND),
    ADD_CAR_AUTHOR_FAILED("APP-41", "Mỗi tài xế chỉ được gán với một xe!", HttpStatus.BAD_REQUEST),
    DRIVER_CANT_BOOK_CAR("APP-42", "Tài xế không thể đặt xe!", HttpStatus.BAD_REQUEST),
    NEW_TRIP_FAIL("APP-43", "Bạn đang đặt xe hoặc đang trong 1 chuyến xe!", HttpStatus.BAD_REQUEST),
    TRIP_NOT_FOUND("APP-43", "Không tìm thấy chuyến đi!", HttpStatus.BAD_REQUEST),
    TRIP_CANCEL_FAIL("APP-44", "Chuyến đi đã được hủy", HttpStatus.BAD_REQUEST),
    TRIP_CANCEL_FAIL_01("APP-45", "Chuyến đi đang trong quá trình", HttpStatus.BAD_REQUEST),
    TRIP_CANCEL_FAIL_02("APP-46", "Chuyến đi đã hoàn thành!", HttpStatus.BAD_REQUEST),
    DRIVER_IS_BUSY("APP-47", "Bạn chưa hoàn thành 1 chuyến xe!", HttpStatus.BAD_REQUEST),
    TRIP_CANCELED_OR_IN_PROGRESS("APP-48", "Chuyến xe đã được hủy hoặc có người khác nhận!", HttpStatus.BAD_REQUEST),
    CURRENT_TRIP_INVALID("APP-49", "Đây không phải là chuyến xe hiện tại của bạn!", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("APP-50", "Status phải là 1 trong 4 giá trị 'NEW' 'IN_PROGRESS' 'COMPLECTED' 'CANCELED'", HttpStatus.BAD_REQUEST),
    INVALID_DRIVER("APP-52", "Bạn không phải tài xế của chuyến đi này!", HttpStatus.BAD_REQUEST),
    NOT_DRIVER("APP-53", "Không phải là một tài xế", HttpStatus.BAD_REQUEST),
    DRIVER_INACTIVE("APP-54", "Tài xế đang không hoat dong. Vui lòng chọn tài xế khác!", HttpStatus.BAD_REQUEST);



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


