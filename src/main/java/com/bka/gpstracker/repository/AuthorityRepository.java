package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.Authority;
import com.bka.gpstracker.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> getAllByUserInfo(UserInfo userInfo);
}
