package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.report.dto.DataAnalysisDto;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataAnalysisService {
    private final LocationRepository locationRepository;
    private final ReportRepository reportRepository;

    public DataAnalysisDto.DangerousTop10LocationListResponse getTopTenGu(){
        Integer rank = 1;
        Integer equalRank = 1;
        Long previousValue = null;

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
                top3GuList.add(dangerousLocationResponse);
            }else{
                otherList.add(dangerousLocationResponse);
            }

        }
        return DataAnalysisDto.DangerousTop10LocationListResponse.builder()
                .top3Location(top3GuList)
                .theOthers(otherList)
                .build();
    }

//    public DataAnalysisDto.DangerousTop10LocationListResponse getTopTenLocation(){
//        Integer rank = 1;
//        Integer previousValue = -1;
//
//        List<DataAnalysisDto.DangerousLocationResponse> top3GuList = new ArrayList<>();
//        List<DataAnalysisDto.DangerousLocationResponse> otherList = new ArrayList<>();
//
//        for (Object[] guInfo : reportRepository.findTop10Gu()){
//            Integer gu = (Integer) guInfo[0];
//            Long reportCount = (Long) guInfo[1];
//            DataAnalysisDto.DangerousLocationResponse dangerousLocationResponse =
//                    new DataAnalysisDto.DangerousLocationResponse(rank, gu, reportCount);
//        }
//        return DataAnalysisDto.DangerousTop10LocationListResponse.builder()
//                .top3Location(top3GuList)
//                .theOthers(otherList)
//                .build();
//    }
}
