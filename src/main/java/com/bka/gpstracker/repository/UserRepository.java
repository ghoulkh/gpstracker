package com.bka.gpstracker.repository;

import com.bka.gpstracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
