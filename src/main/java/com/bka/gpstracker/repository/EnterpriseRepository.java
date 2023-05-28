package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<Enterprise, String> {
}
