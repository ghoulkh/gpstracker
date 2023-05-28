package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "ThongTinXe")
public class CarInfo {
    @Id
    @Column(name = "RFID")
    private String rfid;
    @Column(name = "BienSoXe")
    private String licensePlate;
    @Column(name = "TaiXe")
    private String driver;
    @Column(name = "GiayPhep")
    private String drivingLicense;
    @Column(name = "userName")
    private String username;

}
