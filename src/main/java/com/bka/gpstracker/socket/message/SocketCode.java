package com.bka.gpstracker.socket.message;

public enum SocketCode {
    WARNING_MESSAGE("socket-01", "Tài xế đã lái xe liên tục 8 tiếng, vui lòng nghỉ ngơi");
    private String code;
    private String message;
    private SocketCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}
