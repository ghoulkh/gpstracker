package com.bka.gpstracker.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class CheckInModelSocket {
    private Long id;
    private String rfid;
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    private Date date;
    private boolean enabled;
}
