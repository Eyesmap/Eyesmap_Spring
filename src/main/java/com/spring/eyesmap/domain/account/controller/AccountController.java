package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.service.AccountService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // 사용자 신고 내역 조회
    @PostMapping("/api/account/report/list")
    public BaseResponse<AccountDto.ReportListResponseDto> fetchReportList(@RequestBody AccountDto.FetchReportListRequestDto fetchReportListRequestDto){
        AccountDto.ReportListResponseDto reportListResponseDto = accountService.fetchReportList(fetchReportListRequestDto);
        return new BaseResponse<>(reportListResponseDto);
    }

    // 사용자 공감 내역 조회
    @PostMapping("/api/account/dangerouscnt/list")
    public BaseResponse<AccountDto.DangerousCntListResponseDto> fetchDangerousCntList(@RequestBody AccountDto.FetchDangerousCntListRequestDto fetchDangerousCntListRequestDto){
        AccountDto.DangerousCntListResponseDto dangerousCntListResponseDto = accountService.fetchDangerousCntList(fetchDangerousCntListRequestDto);
        return new BaseResponse<>(dangerousCntListResponseDto);
    }

    // 사용자 정보 조회
    @GetMapping("/api/account/info")
    public BaseResponse<AccountDto.FetchAccountResponseDto> fetchAccount(){
        AccountDto.FetchAccountResponseDto fetchAccountResponseDto = accountService.fetchAccount();
        return new BaseResponse<>(fetchAccountResponseDto);
    }

    // 명예의 전당 랭킹 조회
    @GetMapping("/api/account/ranking/list")
    public BaseResponse<AccountDto.RankingResponseDto> fetchRankingList(){
        AccountDto.RankingResponseDto rankingResponseDto = accountService.fetchRankingList();
        return new BaseResponse<>(rankingResponseDto);
    }

    // 프로필 사진 변경
    @PostMapping("/api/account/profile/image/update")
    public BaseResponse<Void> updateProfileImage(@RequestPart("image") MultipartFile image, @RequestPart AccountDto.UpdateProfileImageReqeuestDto updateProfileImageReqeuestDto) throws IOException {
        accountService.updateProfileImage(image, updateProfileImageReqeuestDto);
        return new BaseResponse<>();
    }

    // 기본 프로필 이미지로 변경(프로필 삭제)
    @GetMapping("/api/account/profile/image/init")
    public BaseResponse<Void> initProfileImage() throws IOException {
        accountService.initProfileImage();
        return new BaseResponse<>();
    }

    // 사용자 모든 정보 가져오기
    @GetMapping("/api/account/all")
    public BaseResponse<Void> fetchAllAccount() {
        accountService.fetchAllAccount();
        return new BaseResponse<>();
    }
}
