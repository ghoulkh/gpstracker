package com.bka.gpstracker.socket.message;

import lombok.Data;

@Data
public class SocketMessageContainer {
    private Object data;
    private Type type;
    public enum Type {
        NEW_TRIP,
        TRIP_IN_PROGRESS,
        CANCEL_TRIP,
        WARNING_MESSAGE,
        NEW_DELIVERY,
        UPDATE_DELIVERY,
        DRIVER_CANCEL_DELIVERY,
        DELIVERY_IN_PROGRESS,
        DELIVERY_CANCEL,
        DELIVERY_COMPLETED;
    }

    public SocketMessageContainer(Type type, Object data) {
        this.data = data;
        this.type = type;
    }
}
