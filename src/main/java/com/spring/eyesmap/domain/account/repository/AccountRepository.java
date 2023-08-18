package com.spring.eyesmap.domain.account.repository;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.dto.RankingList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);

    @Query("SELECT new com.spring.eyesmap.domain.account.dto.RankingList(" +
            "a.userId, a.nickname, a.profileImageUrl, COUNT(r.account)) " +
            "FROM account a " +
            "INNER JOIN report r " +
            "ON a.userId = r.account.userId " +
            "GROUP BY a.userId, a.nickname " +
            "ORDER BY COUNT(r.account) DESC")
    List<RankingList> findTop10Ranking();

}
