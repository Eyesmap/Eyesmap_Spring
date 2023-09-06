package com.spring.eyesmap.domain.report.repository;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findAllByReportedStatus(ReportEnum.ReportedStatus reportedStatus);
    List<Report> findAllByGuAndReportedStatus(Integer gu, ReportEnum.ReportedStatus reportedStatus);
    List<Report> findByAccountAndReportedStatus(Account account, ReportEnum.ReportedStatus reportedStatus);

    @Query("SELECT r.gu, COUNT(r) AS reportCount FROM report r group by r.gu order by reportCount DESC")
    List<Object[]> findTop10Gu();

    @Query("SELECT r FROM report r WHERE r.gu = :guNum AND r.reportDangerousNum > 0 ORDER BY r.reportDangerousNum DESC")
    List<Report> findTop10ReportsByGuOrderByReportDangerousNumDesc(Integer guNum);

}
