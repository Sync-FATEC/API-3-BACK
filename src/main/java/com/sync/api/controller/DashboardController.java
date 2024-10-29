package com.sync.api.controller;

import com.sync.api.dto.project.Dashboard.ProjectClassificationCount;
import com.sync.api.dto.project.Dashboard.ProjectMonthCount;
import com.sync.api.dto.project.Dashboard.ProjectStatusCount;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/count/status/coordinator")
    public ResponseEntity<?> countStatusCoordinator(@RequestParam(required = false) String nameCoordinator){
        try {
            ProjectStatusCount countStatus = dashboardService.countProjectsByStatus(nameCoordinator);
            return ResponseEntity.status(HttpStatus.OK).body(countStatus);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/count/classification/coordinator")
    public ResponseEntity<?> countClassificationCoordinator(@RequestParam(required = false) String nameCoordinator){
        try {
            ProjectClassificationCount countClassification = dashboardService.countProjectsByClassification(nameCoordinator);
            return ResponseEntity.status(HttpStatus.OK).body(countClassification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/count/month/coordinator")
    public ResponseEntity<?> countMonthCoordinator(@RequestParam(required = false) String nameCoordinator){
        try {
            ProjectMonthCount countMonth = dashboardService.countProjectsByMonth(nameCoordinator);
            return ResponseEntity.status(HttpStatus.OK).body(countMonth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
