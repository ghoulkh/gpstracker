package com.bka.gpstracker.model.response;

import com.bka.gpstracker.solr.entity.Position;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class PositionResponse {
    private String id;
    private Date date;
    private String rfid;
    private String lat;
    private String lon;
    private String speed;

    public static PositionResponse from(Position position) {
        PositionResponse result = new PositionResponse();
        BeanUtils.copyProperties(position, result);
        result.setDate(new Date(position.getCreatedAt()));
        return result;
    }
}
