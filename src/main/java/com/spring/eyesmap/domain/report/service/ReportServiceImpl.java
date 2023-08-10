package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.user.domain.User;
import com.spring.eyesmap.domain.user.repository.UserRepository;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl {
    @Value("${report.list.url}")
    private String reportListUrl;
    @Value("${report.api.key}")
    private String reportApiKey;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * * * ")
    private void getReportFromApi(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = currentDateTime.plusDays(7);
        String formattedCurrentDateTime = currentDateTime.format(formatter);
        String formattedEndDateTime = endDateTime.format(formatter);

        List<User> userList = userRepository.findAllByReportingTrue();

        userList.stream().map((user) -> {
            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("key", reportApiKey);
            urlVariables.put("telno", user.getPhoneNum());//7일마다 신고한 사람 체크하고 조회
            urlVariables.put("startDate", formattedCurrentDateTime);
            urlVariables.put("endDate", formattedEndDateTime);
            urlVariables.put("startNum", "1");//api test후 고치기
            urlVariables.put("endNum", "10");//api test후 고치기

            //결과 형태 보고 고쳐야함
            return restTemplate.getForObject(reportListUrl, ReportDto.ReportListResponseFromAPI.class, urlVariables);
        });
    }

//    public BaseResponse<Report> getReport(){
//
//    }
}
