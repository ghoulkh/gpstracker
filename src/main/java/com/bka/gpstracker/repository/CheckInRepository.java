package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    @Query(value = "SELECT * FROM check_in " +
            "WHERE rfid = :rfid " +
            "AND id = (SELECT MAX(id) FROM check_in WHERE rfid = :rfid)", nativeQuery = true)
    Optional<CheckIn> findLatestCheckInByRfid(@Param("rfid") String rfid);
    @Query(value = "SELECT MAX(id) FROM check_in", nativeQuery = true)
    Long getMaxId();
}
