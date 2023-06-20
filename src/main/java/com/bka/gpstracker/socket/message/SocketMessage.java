package com.bka.gpstracker.socket.message;

import com.bka.gpstracker.socket.message.SocketCode;
import lombok.Data;

@Data
public class SocketMessage {
    private String code;
    private String message;

    public SocketMessage(SocketCode socketCode) {
        this.code = socketCode.getCode();
        this.message = socketCode.getMessage();
    }
}
