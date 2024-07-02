package com.bka.gpstracker.entity;

import com.bka.gpstracker.util.JsonMapper;
import com.bka.gpstracker.util.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class CarInfo {
    @Id
    private String rfid;
    private String licensePlate;
    private String driver;
    private String drivingLicense;
    private String username;
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    private Date lastCheckInAt;
    @Lob
    private String activeAreas;

    @OneToMany(mappedBy = "carInfo", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CheckIn> checkIns;

    @OneToMany(mappedBy = "rfid", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PositionLog> positionLogs;


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
        return Utils.getStatusFromLastCheckIn(checkIns);
    }

}
