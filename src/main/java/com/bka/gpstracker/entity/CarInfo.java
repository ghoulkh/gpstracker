package com.bka.gpstracker.entity;

import com.bka.gpstracker.common.DriverStatus;
import com.bka.gpstracker.util.JsonMapper;
import com.bka.gpstracker.util.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    @Lob
    @Column(name = "active_areas")
    private String activeAreas;

    public List<String> getActiveAreas() {
        if (this.activeAreas == null)
            return new ArrayList<>();
        else {
            return JsonMapper.toObject(this.activeAreas, new TypeReference<List<String>>() {});
        }
    }

    public void setActiveAreas(List<String> activeAreas) {
        this.activeAreas = JsonMapper.toJson(activeAreas);
    }

    public String getStatus() {
        return Utils.getStatusFromLastCheckIn(this.lastCheckInAt);
    }

}
