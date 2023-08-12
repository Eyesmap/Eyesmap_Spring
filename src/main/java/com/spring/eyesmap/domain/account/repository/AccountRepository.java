package com.spring.eyesmap.domain.account.repository;

import com.spring.eyesmap.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByReportingTrue();
}
