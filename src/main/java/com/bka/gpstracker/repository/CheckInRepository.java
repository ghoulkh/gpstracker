package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    @Query(value = "SELECT MAX(STT) FROM CheckIn", nativeQuery = true)
    Long getMaxId();
}
