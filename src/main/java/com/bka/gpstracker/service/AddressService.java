package com.bka.gpstracker.service;

import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.City;
import com.bka.gpstracker.model.District;
import com.bka.gpstracker.model.Street;
import com.bka.gpstracker.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AddressService {

    private List<City> cities;
    private List<District> allDistrict;

    @PostConstruct
    private void loadData() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = AddressService.class.getResourceAsStream("/address/dvhcvn.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            this.cities = objectMapper.readValue(inputStream, new TypeReference<List<City>>() {});
            this.allDistrict = new ArrayList<>();
            for (City city : this.cities) {
                this.allDistrict.addAll(city.getDistricts());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getDistrictInAddress(String address) {
        String addressToEqual = reduceAddress(address);
        Map<String, String> mapReduceDistrictAndDistrict = new HashMap<>();
        List<String> districts = getDistrictByCity("Thành phố Hà Nội");
        for (String district : districts) {
            mapReduceDistrictAndDistrict.put(reduceDistrict(district), district);
        }
        for (Map.Entry<String, String> entry : mapReduceDistrictAndDistrict.entrySet()) {
            if (addressToEqual.contains(entry.getKey()))
                return entry.getValue();
        }
        throw new TrackerAppException(ErrorCode.ADDRESS_INVALID);
    }

    private String reduceAddress(String address) {
        return Utils.toEn(address.toLowerCase());
    }
    private String reduceDistrict(String district) {
        String districtToReduce = district.replace("Quận ", "").replace("Huyện ", "").replace("Thị xã ", "").toLowerCase(Locale.ROOT);
        return Utils.toEn(districtToReduce);
    }

    public List<String> getAllCity() {
        return this.cities.stream().map(City::getName).collect(Collectors.toList());
    }

    public List<String> getDistrictByCity(String cityName) {
        for (City city : this.cities) {
            if (city.getName().equals(cityName)) {
                return city.getDistricts().stream().map(District::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    public List<String> getStreetByDistrict(String districtName) {
        for (District district : this.allDistrict) {
            if (district.getName().equals(districtName))
                return district.getStreets().stream().map(Street::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
