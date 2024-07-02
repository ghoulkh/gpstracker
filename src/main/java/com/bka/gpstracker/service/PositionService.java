package com.bka.gpstracker.service;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.NewPositionEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.PositionLogRequest;
import com.bka.gpstracker.model.response.PositionResponse;
import com.bka.gpstracker.repository.PositionLogRepository;
import com.bka.gpstracker.solr.entity.Position;
import com.bka.gpstracker.solr.repository.PositionLogSolrRepository;
import com.bka.gpstracker.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PositionService {
    @Autowired
    private PositionLogRepository positionLogRepository;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PositionLogSolrRepository positionLogSolrRepository;


    @EventListener
    @Async
    public void pushNewPositionToSolr(NewPositionEvent positionEvent) {
        PositionLog positionLog = (PositionLog) positionEvent.getSource();
        positionLogSolrRepository.save(positionLog.toPositionSolr());
        log.info("push to solr new position with rfid {} - position: {}", positionLog.getRfid(), positionLog);
    }

    public List<PositionResponse> getByRange(String rfid, Long startTime, Long endTime) {
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        return positionLogSolrRepository.getByQuery(buildRangeTimeQuery(rfid, startTime, endTime), paging).stream()
                .map(PositionResponse::from).collect(Collectors.toList());
    }

    public List<PositionResponse> getFromSolrByQuery(String query) {
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        return positionLogSolrRepository.getByQuery(query, paging).stream()
                .map(PositionResponse::from).collect(Collectors.toList());
    }

    private String buildRangeTimeQuery(String rfid, Long startTime, Long endTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rfid:").append(rfid).append(" AND ");
        stringBuilder.append("createdAt:[");
        if (startTime != null) {
            stringBuilder.append(startTime);
        } else
            stringBuilder.append("*");
        stringBuilder.append(" TO ");
        if (endTime != null)
            stringBuilder.append(endTime);
        else
            stringBuilder.append("*");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public List<PositionLog> getByRfid(String rfid, int pageIndex, int pageSize) {
//        String username = SecurityUtil.getCurrentUsername();
//        if (!carInfoService.isAuthorCar(username, rfid))
//            throw new TrackerAppException(ErrorCode.ACCESS_DENIED);
        Pageable paging = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return positionLogRepository.findAllByRfid(rfid, paging);
    }

    public PositionLog addPositionLog(PositionLogRequest request) {
        return positionLogRepository.save(request.toPositionLog((positionLogRepository.getMaxId() == null
                ? 0 : positionLogRepository.getMaxId()) + 1));
    }
}
