package com.bka.gpstracker.entity;

import com.bka.gpstracker.common.DriverStatus;
import com.bka.gpstracker.util.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "last_check_in_at")
    private Date lastCheckInAt;

    public String getStatus() {
        return Utils.getStatusFromLastCheckIn(this.lastCheckInAt);
    }

}
