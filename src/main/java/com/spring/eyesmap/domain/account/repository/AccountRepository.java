package com.spring.eyesmap.domain.account.repository;

import com.spring.eyesmap.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
