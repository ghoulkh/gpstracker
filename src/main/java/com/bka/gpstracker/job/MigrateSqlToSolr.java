package com.bka.gpstracker.job;

import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.repository.PositionLogRepository;
import com.bka.gpstracker.solr.entity.Position;
import com.bka.gpstracker.solr.repository.PositionLogSolrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
@RequiredArgsConstructor
public class MigrateSqlToSolr {
    private final PositionLogRepository positionLogSql;
    private final PositionLogSolrRepository positionLogSolr;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào 12h đêm hàng ngày
    @Transactional
    public void jobMigrate() {
        long start = System.currentTimeMillis();
        log.info("Job migrate start: {}", System.currentTimeMillis());
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Position> entitySolrs = positionLogSolr.getByQuery("*:*", paging);
        for (Position entitySolr : entitySolrs) {
            try {
                if (!positionLogSql.findById(Long.valueOf(entitySolr.getId())).isPresent()) {
                    positionLogSolr.delete(entitySolr);
                }
            } catch (Exception e) {
                log.info("ERROR migrate data: {}", e.getMessage(), e);
                positionLogSolr.delete(entitySolr);
            }
        }


        try {
            List<PositionLog> entitySqls = positionLogSql.findAll();
            for (PositionLog entitySql : entitySqls) {
                Position entitySolrNew = new Position();
                entitySolrNew.setId(String.valueOf(entitySql.getId()));
                entitySolrNew.setRfid(entitySql.getRfid());
                entitySolrNew.setSpeed(entitySql.getSpeed());
                entitySolrNew.setLon(entitySql.getLon());
                entitySolrNew.setLat(entitySql.getLat());
                entitySolrNew.setCreatedAt(entitySql.getDate().getTime());
                positionLogSolr.save(entitySolrNew);
            }
            log.info("Job migrate done: {}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.info("ERROR migrate data: {}", e.getMessage(), e);
        }

    }

}
