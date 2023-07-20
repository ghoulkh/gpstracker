package com.bka.gpstracker.model;

import com.bka.gpstracker.common.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class StatusHistory {
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    private Long createdAt;

}
