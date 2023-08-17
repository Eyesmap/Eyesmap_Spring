package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.service.AccountService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // 사용자 신고 내역 조회
    @GetMapping("/api/account/report/list")
    public BaseResponse<AccountDto.ReportListResponseDto> fetchReportList(){
        AccountDto.ReportListResponseDto reportListResponseDto = accountService.fetchReportList();
        return new BaseResponse<>(reportListResponseDto);
    }

    // 사용자 공감 내역 조회
    @GetMapping("/api/account/dangerouscnt/list")
    public BaseResponse<AccountDto.DangerousCntListResponseDto> fetchDangerousCntList(){
        AccountDto.DangerousCntListResponseDto dangerousCntListResponseDto = accountService.fetchDangerousCntList();
        return new BaseResponse<>(dangerousCntListResponseDto);
    }
}
