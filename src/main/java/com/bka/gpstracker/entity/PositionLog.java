package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "DinhVi")
public class PositionLog {
    @Id
    @Column(name = "STT")
    private Long id;
    @Column(name = "Ngay")
    private String date;
    @Column(name = "Gio")
    private String hour;
    @Column(name = "rfid")
    private String rfid;
    @Column(name = "Lat")
    private String lat;
    @Column(name = "Long")
    private String lon;
}
