package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.NewPositionEvent;
import com.bka.gpstracker.model.DriveContainer;
import com.bka.gpstracker.socket.SocketSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class WarningService {

    private Map<String, DriveContainer> driveContainerMap = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private SocketSender socketSender;
    @Autowired
    private CarInfoService carInfoService;
    @EventListener
    @Async
    public void handleNewPosition(NewPositionEvent event) {
        PositionLog positionLog = (PositionLog) event.getSource();
        DriveContainer driveContainer = driveContainerMap.get(positionLog.getRfid());
        if (driveContainer == null) {
            DriveContainer driveContainerToAdd = new DriveContainer();
            driveContainerToAdd.setDriver(positionLog.getRfid());
            driveContainerToAdd.setLastUpdatePosition(System.currentTimeMillis());
            driveContainerToAdd.setCreatedAt(System.currentTimeMillis());
            driveContainerMap.put(positionLog.getRfid(), driveContainerToAdd);
            log.info("add new car is working to cache with rfid: {}", positionLog.getRfid());
        } else {
            driveContainer.setLastUpdatePosition(System.currentTimeMillis());
            driveContainerMap.put(positionLog.getRfid(), driveContainer);
            log.info("update last updated position with rfid: {}", positionLog.getRfid());
        }
    }


    @Scheduled(initialDelay = 5, fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void jobRemoveCarNotWorking() {
        Long thirtySecondsAgo = System.currentTimeMillis() - 30000;
        Long eightHoursAgo = System.currentTimeMillis() - 28800000;
        for (Map.Entry<String, DriveContainer> entry : driveContainerMap.entrySet()) {
            DriveContainer driveContainer = entry.getValue();
            if (driveContainer.getLastUpdatePosition() > thirtySecondsAgo) {
                driveContainerMap.remove(entry.getKey());
                log.info("remove car not working from drive cache with rfid: {}", entry.getKey());
                return;
            }
            if (driveContainer.getCreatedAt() < eightHoursAgo) {
                carInfoService.findByRfid(driveContainer.getDriver()).ifPresent(carInfo -> {
                    socketSender.sendWarningToDriver(carInfo.getUsername());
                    log.info("send waring to driver done with username: {}", carInfo.getUsername());
                });
            }
        }
    }
}
