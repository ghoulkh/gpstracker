package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.PositionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionLogRepository extends JpaRepository<PositionLog, Long> {
    List<PositionLog> findAllByRfid(String rfid, Pageable pageable);
    @Query(value = "SELECT MAX(id) From position_log", nativeQuery = true)
    Long getMaxId();
}
