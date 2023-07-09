package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.CheckIn;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.repository.CarInfoRepository;
import com.bka.gpstracker.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarInfoService {
    @Autowired
    private CheckInRepository checkInRepository;
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

    public void testCheckIn(String rfid) {
        CheckIn checkIn = new CheckIn();
        checkIn.setDate(new Date());
        checkIn.setRfid(rfid);
        checkIn.setId(checkInRepository.getMaxId() + 1L);
        checkInRepository.save(checkIn);
    }

    public Optional<CarInfo> findByRfid(String rfid) {
        return carInfoRepository.findById(rfid);
    }

    public List<CarInfo> getAllByUsername(String username) {
        return carInfoRepository.findAllByUsername(username);
    }
    public List<CarInfo> getByUsername(String username) {
        return carInfoRepository.findAllByUsername(username);
    }

    public CarInfo save(CarInfo carInfo) {
        return carInfoRepository.save(carInfo);
    }
}
