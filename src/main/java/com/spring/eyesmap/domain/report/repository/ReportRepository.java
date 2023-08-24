package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findAllByReportedStatus(ReportEnum.ReportedStatus reportedStatus);
    List<Report> findAllByGuAndReportedStatus(Integer gu, ReportEnum.ReportedStatus reportedStatus);
    List<Report> findByAccount(Account account);
}
