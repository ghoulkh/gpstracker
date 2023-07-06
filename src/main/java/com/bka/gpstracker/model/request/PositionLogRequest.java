package com.bka.gpstracker.model.request;

import com.bka.gpstracker.entity.PositionLog;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PositionLogRequest {
    @NotBlank(message = "rfid is required!")
    private String rfid;
    @NotBlank(message = "lat is required!")
    private String lat;
    @NotBlank(message = "lon is required!")
    private String lon;
    @NotBlank(message = "speed is required!")
    private String speed;


    public PositionLog toPositionLog(Long id) {
        PositionLog positionLog = new PositionLog();
        positionLog.setDate(new Date());
        positionLog.setRfid(this.rfid);
        positionLog.setSpeed(this.speed);
        positionLog.setLat(this.lat);
        positionLog.setLon(this.lon);
        positionLog.setId(id);
        return positionLog;
    }
}
