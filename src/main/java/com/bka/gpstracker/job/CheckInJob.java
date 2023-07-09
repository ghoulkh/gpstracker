package com.bka.gpstracker.job;

import com.bka.gpstracker.event.NewCheckInEvent;
import com.bka.gpstracker.repository.CheckInRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Log4j2
public class CheckInJob {
    private Long currentId;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CheckInRepository checkInRepository;

    @Scheduled(initialDelay = 10, fixedDelay = 3, timeUnit = TimeUnit.SECONDS)
    private void jobCheckNewCheckIn() {
        if (this.currentId == null)
            this.currentId = checkInRepository.getMaxId();
        Long maxId = checkInRepository.getMaxId();
        if (maxId > this.currentId) {
            for (Long i = currentId + 1; i <= maxId; i ++) {
                checkInRepository.findById(i).ifPresent(checkIn -> {
                    applicationEventPublisher.publishEvent(new NewCheckInEvent(checkIn));
                });
            }
            this.currentId = maxId;
        }
    }
}
