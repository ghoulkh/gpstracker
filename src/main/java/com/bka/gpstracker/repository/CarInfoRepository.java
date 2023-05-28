package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarInfoRepository extends JpaRepository<CarInfo, String> {
    List<CarInfo> findAllByUsername(String username);

}
