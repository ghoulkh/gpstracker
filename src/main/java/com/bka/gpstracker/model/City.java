package com.bka.gpstracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class City {
    private String name;
    @JsonProperty("level2s")
    private List<District> districts;
}
