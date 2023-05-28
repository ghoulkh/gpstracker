package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.repository.CarInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarInfoService {
    @Autowired
    private CarInfoRepository carInfoRepository;

    public boolean isAuthorCar(String username, String rfid) {
        Set<String> rfids = carInfoRepository.findAllByUsername(username).stream().map(CarInfo::getRfid).collect(Collectors.toSet());
        return rfids.contains(rfid);
    }

    public List<CarInfo> getCarsInfo(int pageIndex, int pageSize) {
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize);
        return carInfoRepository.findAll(paging).getContent();
    }

    public CarInfo getByRfid(String rfid) {
        return carInfoRepository.findById(rfid).orElseThrow(() ->
                new TrackerAppException(ErrorCode.CAR_INFO_NOT_FOUND));
    }

    public CarInfo save(CarInfo carInfo) {
        return carInfoRepository.save(carInfo);
    }
}
