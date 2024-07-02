package com.bka.gpstracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class CheckIn {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "rfid")
    @JsonBackReference
    private CarInfo carInfo;
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    private Date date;
    private boolean enabled;
}
