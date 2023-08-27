package com.spring.eyesmap.domain.report.controller;

import com.spring.eyesmap.domain.report.dto.DataAnalysisDto;
import com.spring.eyesmap.domain.report.service.DataAnalysisService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataAnalysisController {
    private final DataAnalysisService dataAnalysisService;

    @GetMapping("gu/ranking/list")
    public BaseResponse<DataAnalysisDto.DangerousTop10LocationListResponse> getTopTenGu(){
        return new BaseResponse<>(dataAnalysisService.getTopTenGu());
    }
}
