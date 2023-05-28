package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.event.NewPositionEvent;
import com.bka.gpstracker.repository.PositionLogRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class PositionJob {

    @Autowired
    private PositionLogRepository positionLogRepository;
    private Long lastId;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
//    private PositionLog positionLog;

    @PostConstruct
    private void updateCurrentPositionId() {
        this.lastId = positionLogRepository.getMaxId();
//       this.positionLog = positionLogRepository.findById(150L).get();
    }

    @Scheduled(initialDelay = 5, fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void checkNewPosition() {
        Long currentId = positionLogRepository.getMaxId();
        if (lastId < currentId) {
            lastId ++;
            for (Long i = lastId; i <= currentId; i ++) {
                positionLogRepository.findById(i).ifPresent(positionLog -> {
                    applicationEventPublisher.publishEvent(new NewPositionEvent(positionLog));
                    log.info("Publish event new position done!");
                });
            }
        }
    }

//    @Scheduled(initialDelay = 5, fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
//    public void test() {
//        this.positionLog.setId(positionLogRepository.getMaxId() + 1);
//        this.positionLog.setRfid("AA123456");
//        PositionLog positionLogToSave = new PositionLog();
//        BeanUtils.copyProperties(this.positionLog, positionLogToSave);
//        positionLogRepository.save(positionLogToSave);
//    }
}
