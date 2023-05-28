package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.PositionLogRequest;
import com.bka.gpstracker.repository.PositionLogRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionLogRepository positionLogRepository;
    @Autowired
    private CarInfoService carInfoService;


    public List<PositionLog> getByRfid(String rfid, int pageIndex, int pageSize) {
        String username = SecurityUtil.getCurrentUsername();
        if (!carInfoService.isAuthorCar(username, rfid))
            throw new TrackerAppException(ErrorCode.ACCESS_DENIED);
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return positionLogRepository.findAllByRfid(rfid, paging);
    }

    public PositionLog addPositionLog(PositionLogRequest request) {
        return positionLogRepository.save(request.toPositionLog(positionLogRepository.getMaxId() + 1));
    }
}
