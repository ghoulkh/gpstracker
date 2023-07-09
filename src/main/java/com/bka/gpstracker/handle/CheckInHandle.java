package com.bka.gpstracker.handle;

import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.CheckIn;
import com.bka.gpstracker.event.NewCheckInEvent;
import com.bka.gpstracker.repository.CarInfoRepository;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CheckInHandle {

    @Autowired
    private CarInfoRepository carInfoRepository;
    @EventListener
    @Async
    public void handleNewCheckIn(NewCheckInEvent event) {
        CheckIn checkIn = (CheckIn) event.getSource();
        CarInfo carInfo = carInfoRepository.getByRfid(checkIn.getRfid());
        if (carInfo == null) return;
        carInfo.setLastCheckInAt(checkIn.getDate());
        carInfoRepository.save(carInfo);
        log.info("checkIn with car {}", carInfo);
    }
}
