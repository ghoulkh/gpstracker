package com.bka.gpstracker.handle;

import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.CheckIn;
import com.bka.gpstracker.event.NewCheckInEvent;
import com.bka.gpstracker.repository.CarInfoRepository;
import com.bka.gpstracker.socket.SocketSender;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.bka.gpstracker.solr.repository.UserInfoRepository;
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
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private SocketSender socketSender;

    @EventListener
    @Async
    public void handleNewCheckIn(NewCheckInEvent event) {
        CheckIn checkIn = (CheckIn) event.getSource();
        CarInfo carInfo = carInfoRepository.getByRfid(checkIn.getCarInfo().getRfid());
        if (carInfo == null) return;
        carInfo.setLastCheckInAt(checkIn.getDate());
        carInfoRepository.save(carInfo);
        UserInfo userInfo = userInfoRepository.getByUsername(carInfo.getUsername());
        userInfo.setLastCheckInAt(checkIn.getDate());
        userInfoRepository.save(userInfo);
        socketSender.sendCheckInToUser(checkIn);
        log.info("checkIn and send check in to user with car {}", carInfo);
    }
}
