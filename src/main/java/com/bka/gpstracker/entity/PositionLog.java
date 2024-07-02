package com.bka.gpstracker.entity;

import com.bka.gpstracker.solr.entity.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class PositionLog {
    @Id
    private Long id;
    @JsonFormat(timezone = "Asia/Ho_Chi_Minh")
    private Date date;
    private String rfid;
    private String lat;
    private String lon;
    private String speed;

    public Position toPositionSolr() {
        Position position = new Position();
        position.setId(this.id + "");
        position.setCreatedAt(date.getTime());
        position.setRfid(this.rfid);
        position.setLat(this.lat);
        position.setLon(this.lon);
        position.setSpeed(this.speed);
        return position;
    }
}
