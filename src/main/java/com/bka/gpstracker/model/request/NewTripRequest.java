package com.bka.gpstracker.model.request;


import com.bka.gpstracker.common.TripStatus;
import com.bka.gpstracker.solr.entity.Trip;
import lombok.Data;
import org.eclipse.jetty.util.ArrayTernaryTrie;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Data
public class NewTripRequest {
    @NotBlank(message = "fromAddress is required!")
    private String fromAddress;
    @NotBlank(message = "toAddress is required!")
    private String toAddress;
    @NotBlank(message = "fromLat is required!")
    private String fromLat;
    @NotBlank(message = "fromLon is required!")
    private String fromLon;
    @NotBlank(message = "toLat is required!")
    private String toLat;
    @NotBlank(message = "toLon is required!")
    private String toLon;
    private String notes;

    public Trip toTrip(String currentUsername) {
        Trip result = new Trip();
        BeanUtils.copyProperties(this, result);
        result.setCreatedAt(new Date());
        result.setCreatedBy(currentUsername);
        result.setId(UUID.randomUUID().toString());
        result.setStatus(TripStatus.NEW);
        return result;
    }
}
