package com.bka.gpstracker.socket.message;

import lombok.Data;

@Data
public class SocketMessageContainer {
    private Object data;
    private Type type;
    public enum Type {
        NEW_TRIP,
        WARNING_MESSAGE;
    }

    public SocketMessageContainer(Type type, Object data) {
        this.data = data;
        this.type = type;
    }
}
