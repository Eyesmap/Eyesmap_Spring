package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.dto.DataAnalysisDto;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class DataAnalysisService {
    @Value("${medal.img.basic.url}")
    private String medalPath;
    private final LocationRepository locationRepository;
    private final ReportRepository reportRepository;
    private final S3UploaderService s3UploaderService;

    public DataAnalysisDto.DangerousTop10GuListResponse getTopTenGu(){
        Integer rank = 1;
        Integer equalRank = 1;
        Long previousValue = null;
        String medal = null;

        List<DataAnalysisDto.DangerousLocationResponse> top3GuList = new ArrayList<>();
        List<DataAnalysisDto.DangerousLocationResponse> otherList = new ArrayList<>();

        for (Object[] guInfo : reportRepository.findTop10Gu()){
            Integer gu = (Integer) guInfo[0];
            Long reportCount = (Long) guInfo[1];

            if(previousValue == null){
                previousValue = reportCount;//55
            }
            else if(previousValue > reportCount) {//55 55 50 50 50 47
                previousValue = reportCount;//55
                rank += equalRank; //1 3 6
                equalRank = 1;
            }else{
                equalRank++;//2 23
            }

            DataAnalysisDto.DangerousLocationResponse dangerousLocationResponse =
                    new DataAnalysisDto.DangerousLocationResponse(rank, gu, reportCount);

            if(rank<=3){
                if(rank == 1){
                    medal = s3UploaderService.getS3(medalPath + "gold.png");
                    dangerousLocationResponse.setMedal(medal);
                }else if(rank == 2){
                    medal = s3UploaderService.getS3(medalPath + "silver.png");
                    dangerousLocationResponse.setMedal(medal);
                } else{
                    medal = s3UploaderService.getS3(medalPath + "bronze.png");
                    dangerousLocationResponse.setMedal(medal);
                }
                top3GuList.add(dangerousLocationResponse);
            }else{
                otherList.add(dangerousLocationResponse);
            }

        }
        return DataAnalysisDto.DangerousTop10GuListResponse.builder()
                .top3Location(top3GuList)
                .theOthers(otherList)
                .build();
    }

    public DataAnalysisDto.DangerousTop10ReportListResponse getTopTenReportPerGu(Integer guId){
        Integer rank = 1;
        Integer equalRank = 1;
        Integer previousValue = null;
        String medal = null;

        List<DataAnalysisDto.DangerousReportPerGuResponse> top3ReportPerGuList = new ArrayList<>();
        List<DataAnalysisDto.DangerousReportPerGuResponse> otherList = new ArrayList<>();

        for (Report reportInfo : reportRepository.findTop10ReportsByGuOrderByReportDangerousNumDesc(guId)){
            Integer reportDangerousCnt = reportInfo.getReportDangerousNum();
            if(previousValue == null){
                previousValue = reportDangerousCnt;//55
            }
            else if(previousValue > reportDangerousCnt) {//55 55 50 50 50 47
                previousValue = reportDangerousCnt;//55
                rank += equalRank; //1 3 6
                equalRank = 1;
            }else{
                equalRank++;//2 23
            }

            DataAnalysisDto.DangerousReportPerGuResponse dangerousReportPerGuResponse =
                    new DataAnalysisDto.DangerousReportPerGuResponse(reportInfo, rank);

            if(rank<=3){
                if(rank == 1){
                    medal = s3UploaderService.getS3(medalPath + "gold.png");
                    dangerousReportPerGuResponse.setMedal(medal);
                }else if(rank == 2){
                    medal = s3UploaderService.getS3(medalPath + "silver.png");
                    dangerousReportPerGuResponse.setMedal(medal);
                } else{
                    medal = s3UploaderService.getS3(medalPath + "bronze.png");
                    dangerousReportPerGuResponse.setMedal(medal);
                }
                top3ReportPerGuList.add(dangerousReportPerGuResponse);
            }else{
                otherList.add(dangerousReportPerGuResponse);
            }

        }
        return DataAnalysisDto.DangerousTop10ReportListResponse.builder()
                .top3Report(top3ReportPerGuList)
                .theOthers(otherList)
                .build();
    }
}
