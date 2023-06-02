package com.bka.gpstracker.handle;

import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.InactiveDriverEvent;
import com.bka.gpstracker.event.NewDriverActive;
import com.bka.gpstracker.event.NewPositionEvent;
import com.bka.gpstracker.repository.CarInfoRepository;
import com.bka.gpstracker.service.DriverService;
import com.bka.gpstracker.service.PositionService;
import com.bka.gpstracker.solr.entity.CurrentPosition;
import com.bka.gpstracker.solr.repository.CurrentPositionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Log4j2
public class CurrentPositionHandle {
    @Autowired
    private CurrentPositionRepository currentPositionRepository;
    @Autowired
    private CarInfoRepository carInfoRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private PositionService positionService;
    @EventListener
    @Async
    public void handleNewPosition(NewPositionEvent newPositionEvent) {
        PositionLog positionLog = (PositionLog) newPositionEvent.getSource();
        CarInfo carInfo = carInfoRepository.getById(positionLog.getRfid());
        if (carInfo == null) {
            log.info("carInfo not found with positionLog {}", positionLog);
            return;
        }
        Set<String> usernames = driverService.getActiveUsernames();
        if (!usernames.contains(carInfo.getUsername())) {
            log.info("new positionLog but driver not active");
            return;
        }
        CurrentPosition currentPosition = new CurrentPosition();
        currentPositionRepository.save(currentPosition);
        log.info("update current position with driver active done username: {}", carInfo.getUsername());
    }
    @EventListener
    @Async
    public void handleNewActiveDriver(NewDriverActive newDriverActive) {
        String username = (String) newDriverActive.getSource();
        List<CarInfo> carInfos = carInfoRepository.findAllByUsername(username);
        if (carInfos.isEmpty()) {
            log.info("carInfo not found with positionLog {}", username);
            return;
        }
        CarInfo carInfo = carInfos.get(0);

        List<PositionLog> positionLogs = positionService.getByRfid(carInfo.getRfid(), 1, 1);
        if (positionLogs.isEmpty()) {
            log.info("positionLogs not found with rfid: {}", carInfo.getRfid());
        }

        PositionLog currentPosition = positionLogs.get(0);
        CurrentPosition currentPositionToSave = new CurrentPosition();
        currentPositionToSave.setLat(currentPosition.getLat());
        currentPositionToSave.setLon(currentPosition.getLon());
        currentPositionToSave.setUsername(carInfo.getUsername());
        currentPositionRepository.save(currentPositionToSave);
        log.info("Update current position done with username: {}", carInfo.getUsername());
    }
    @EventListener
    @Async
    public void handleInactiveDriver(InactiveDriverEvent inactiveDriverEvent) {
        currentPositionRepository.deleteById((String) inactiveDriverEvent.getSource());
        log.info("delete current position with driver done!");
    }
}
