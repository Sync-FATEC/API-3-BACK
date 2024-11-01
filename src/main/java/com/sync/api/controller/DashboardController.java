package com.sync.api.controller;

import com.sync.api.dto.project.Dashboard.ProjectClassificationCount;
import com.sync.api.dto.project.Dashboard.ProjectInvestment;
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
    public ResponseEntity<?> countStatusCoordinator(@RequestParam(required = false) String nameCoordinator,
                                                    @RequestParam(required = false) String projectStartDate,
                                                    @RequestParam(required = false) String projectEndDate){
        try {
            ProjectStatusCount countStatus = dashboardService.countProjectsByStatus(nameCoordinator, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar status de projetos: " + e.getMessage());
        }
    }

    @GetMapping("/count/classification/coordinator")
    public ResponseEntity<?> countClassificationCoordinator(@RequestParam(required = false) String nameCoordinator,
                                                            @RequestParam(required = false) String projectStartDate,
                                                            @RequestParam(required = false) String projectEndDate){
        try {
            ProjectClassificationCount countClassification = dashboardService.countProjectsByClassification(nameCoordinator, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countClassification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar classificações: " + e.getMessage());
        }
    }

    @GetMapping("/count/month/coordinator")
    public ResponseEntity<?> countMonthCoordinator(@RequestParam(required = false) String nameCoordinator,
                                                   @RequestParam(required = false) String projectStartDate,
                                                   @RequestParam(required = false) String projectEndDate){
        try {
            ProjectMonthCount countMonth = dashboardService.countProjectsByMonth(nameCoordinator, projectStartDate, projectEndDate);
            System.out.println(projectStartDate);
            System.out.println(projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countMonth);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar meses: " + e.getMessage());
        }
    }

    @GetMapping("/count/status/company")
    public ResponseEntity<?> countStatusCompany(
            @RequestParam(required = false) String projectCompany,
            @RequestParam(required = false) String projectStartDate,
            @RequestParam(required = false) String projectEndDate) {
        try {
            ProjectStatusCount countStatus = dashboardService.countProjectsByStatusForCompany(projectCompany, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar status de projetos: " + e.getMessage());
        }
    }

    @GetMapping("/count/classification/company")
    public ResponseEntity<?> countClassificationCompany(@RequestParam(required = false) String projectCompany,
                                                        @RequestParam(required = false) String projectStartDate,
                                                        @RequestParam(required = false) String projectEndDate) {
        try {
            ProjectClassificationCount countClassification = dashboardService.countProjectsByClassificationForCompany(projectCompany, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countClassification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar classificações: " + e.getMessage());
        }
    }

    @GetMapping("/count/month/company")
    public ResponseEntity<?> countMonthCompany(@RequestParam(required = false) String projectCompany,
                                               @RequestParam(required = false) String projectStartDate,
                                               @RequestParam(required = false) String projectEndDate) {
        try {
            ProjectMonthCount countMonth = dashboardService.countProjectsByMonthForCompany(projectCompany, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(countMonth);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao contar meses: " + e.getMessage());
        }
    }

    @GetMapping("/count/investment/company")
    public ResponseEntity<?> countInvestmentByCompany(@RequestParam(required = false) String projectCompany,
                                                      @RequestParam(required = false) String projectStartDate,
                                                      @RequestParam(required = false) String projectEndDate) {
        try {
            ProjectInvestment investment = dashboardService.calculateInvestmentByCompany(projectCompany, projectStartDate, projectEndDate);
            return ResponseEntity.status(HttpStatus.OK).body(investment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao calcular o investimento: " + e.getMessage());
        }
    }
}
