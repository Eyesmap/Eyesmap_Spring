package com.spring.eyesmap.domain.report.controller;

import com.spring.eyesmap.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create")
    public String createReport(@RequestParam("images") MultipartFile images) throws IOException {//List<MultipartFile>
        reportService.createReport(images, "report");
        return "done";
    }
}
