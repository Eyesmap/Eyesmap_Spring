package com.spring.eyesmap.domain.user.repository;

import com.spring.eyesmap.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByReportingTrue();
}
