package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "CheckIn")
public class CheckIn {
    @Id
    @Column(name = "STT")
    private Long id;
    @Column(name = "RFID")
    private String rfid;
    @Column(name = "Time")
    private Date date;
}
