package com.bka.gpstracker.model;

import lombok.Data;

@Data
public class DriveContainer {
    private String driver;
    private Long lastUpdatePosition;
    private Long createdAt;
}
