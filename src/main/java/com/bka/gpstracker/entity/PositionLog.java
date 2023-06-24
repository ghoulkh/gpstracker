package com.bka.gpstracker.entity;

import com.bka.gpstracker.solr.entity.Position;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Position toPositionSolr() {
        Position position = new Position();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        position.setId(this.id + "");
        String dateString = this.date + " " + this.hour;
        try {
            position.setCreatedAt(simpleDateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            position.setCreatedAt(System.currentTimeMillis());
        }
        position.setRfid(this.rfid);
        position.setLat(this.lat);
        position.setLon(this.lon);
        return position;
    }
}
