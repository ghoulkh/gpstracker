package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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
    @Column(name = "last_check_in_at")
    private Date lastCheckInAt;

    public String getStatus() {
        if (this.lastCheckInAt == null) {
            return "INACTIVE";
        }
        Date sixHoursAgo = new Date(System.currentTimeMillis() - 3600 * 1000 * 6);
        if (this.lastCheckInAt.before(sixHoursAgo)) {
            return "INACTIVE";
        }
        return "ACTIVE";
    }

}
