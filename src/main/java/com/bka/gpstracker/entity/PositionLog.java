package com.bka.gpstracker.entity;

import com.bka.gpstracker.solr.entity.Position;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "DinhVi")
public class PositionLog {
    @Id
    @Column(name = "STT")
    private Long id;
    @Column(name = "Time")
    private Date date;
    @Column(name = "rfid")
    private String rfid;
    @Column(name = "Lat")
    private String lat;
    @Column(name = "Long")
    private String lon;
    @Column(name = "Speed")
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
