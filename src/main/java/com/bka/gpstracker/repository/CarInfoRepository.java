package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarInfoRepository extends JpaRepository<CarInfo, String> {
    List<CarInfo> findAllByUsername(String username);
    CarInfo getByRfid(String rfid);
    Optional<CarInfo> findByUsername(String username);
}
