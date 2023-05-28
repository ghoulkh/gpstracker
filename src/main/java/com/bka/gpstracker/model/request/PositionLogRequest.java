package com.bka.gpstracker.model.request;

import com.bka.gpstracker.entity.PositionLog;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PositionLogRequest {
    @NotBlank(message = "date is required!")
    private String date;
    @NotBlank(message = "hour is required!")
    private String hour;
    @NotBlank(message = "rfid is required!")
    private String rfid;
    @NotBlank(message = "lat is required!")
    private String lat;
    @NotBlank(message = "lon is required!")
    private String lon;

    public PositionLog toPositionLog(Long id) {
        PositionLog positionLog = new PositionLog();
        positionLog.setDate(this.date);
        positionLog.setHour(this.hour);
        positionLog.setRfid(this.rfid);
        positionLog.setLat(this.lat);
        positionLog.setLon(this.lon);
        positionLog.setId(id);
        return positionLog;
    }
}
