package com.sync.api.service;

import com.sync.api.dto.project.Dashboard.ProjectClassificationCount;
import com.sync.api.dto.project.Dashboard.ProjectInvestment;
import com.sync.api.dto.project.Dashboard.ProjectMonthCount;
import com.sync.api.dto.project.Dashboard.ProjectStatusCount;
import com.sync.api.enums.ProjectClassification;
import com.sync.api.enums.ProjectStatus;
import com.sync.api.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectStatusCount countProjectsByStatus(String coordinatorName, String startDate, String endDate) {

        LocalDate projectStartDate = null;
        LocalDate projectEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            projectStartDate = YearMonth.parse(startDate).atDay(1);
        }

        if (endDate != null && !endDate.isEmpty()) {
            projectEndDate = YearMonth.parse(endDate).atEndOfMonth();
        }

        List<Object[]> resultList = projectRepository.countProjectsByStatusCoordinator(
                coordinatorName, projectStartDate, projectEndDate
        );

        Long naoIniciados = 0L;
        Long emAndamento = 0L;
        Long finalizados = 0L;

        if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 3) {
            Object[] resultArray = resultList.get(0);
            naoIniciados = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
            emAndamento = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            finalizados = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
        }
        return new ProjectStatusCount(naoIniciados, emAndamento, finalizados);
    }

    public ProjectClassificationCount countProjectsByClassification(String coordinatorName, String startDate, String endDate) {

        LocalDate projectStartDate = null;
        LocalDate projectEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            projectStartDate = YearMonth.parse(startDate).atDay(1);
        }

        if (endDate != null && !endDate.isEmpty()) {
            projectEndDate = YearMonth.parse(endDate).atEndOfMonth();
        }
        List<Object[]> resultList =projectRepository.countProjectsByClassificationCoordinator(
                coordinatorName, projectStartDate, projectEndDate
        );

        Long outros =0L;
        Long contratos =0L;
        Long convenio=0L;
        Long patrocinio=0L;
        Long termoDeCooperacao=0L;
        Long termoDeOutorga=0L;

        if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 6) {
            Object[] resultArray = resultList.get(0);
            outros = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
            contratos = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            convenio = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            patrocinio = (resultArray[3] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            termoDeCooperacao = (resultArray[4] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            termoDeOutorga = (resultArray[5] != null) ? ((Number) resultArray[2]).longValue() : 0L;
        }

        return new ProjectClassificationCount(outros,contratos,convenio, patrocinio, termoDeCooperacao, termoDeOutorga);
    }

    public ProjectMonthCount countProjectsByMonth(String coordinatorName, String startDate, String endDate) {

        LocalDate projectStartDate = null;
        LocalDate projectEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            projectStartDate = YearMonth.parse(startDate).atDay(1);
        }

        if (endDate != null && !endDate.isEmpty()) {
            projectEndDate = YearMonth.parse(endDate).atEndOfMonth();
        }
        List<Object[]> results = projectRepository.countProjectsByMonthCoordinator(
                coordinatorName, projectStartDate, projectEndDate
        );

        Long janeiro = 0L, fevereiro = 0L, marco = 0L, abril = 0L, maio = 0L, junho = 0L;
        Long julho = 0L, agosto = 0L, setembro = 0L, outubro = 0L, novembro = 0L, dezembro = 0L;

        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];

            if (month == null) {
                continue;
            }

            switch (month) {
                case 1: janeiro = count; break;
                case 2: fevereiro = count; break;
                case 3: marco = count; break;
                case 4: abril = count; break;
                case 5: maio = count; break;
                case 6: junho = count; break;
                case 7: julho = count; break;
                case 8: agosto = count; break;
                case 9: setembro = count; break;
                case 10: outubro = count; break;
                case 11: novembro = count; break;
                case 12: dezembro = count; break;
            }
        }
        return new ProjectMonthCount(janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro);
    }

    public ProjectStatusCount countProjectsByStatusForCompany() {
        List<Object[]> resultList = projectRepository.countProjectsByStatusForCompany();

        Long naoIniciados = 0L;
        Long emAndamento = 0L;
        Long finalizados = 0L;

        if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 3) {
            Object[] resultArray = resultList.get(0);
            naoIniciados = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
            emAndamento = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            finalizados = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
        }
        return new ProjectStatusCount(naoIniciados, emAndamento, finalizados);
    }

    public ProjectClassificationCount countProjectsByClassificationForCompany() {
        List<Object[]> resultList = projectRepository.countProjectsByClassificationForCompany();

        Long outros = 0L, contratos = 0L, convenio = 0L, patrocinio = 0L, termoDeCooperacao = 0L, termoDeOutorga = 0L;

        if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 6) {
            Object[] resultArray = resultList.get(0);
            outros = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
            contratos = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            convenio = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            patrocinio = (resultArray[3] != null) ? ((Number) resultArray[3]).longValue() : 0L;
            termoDeCooperacao = (resultArray[4] != null) ? ((Number) resultArray[4]).longValue() : 0L;
            termoDeOutorga = (resultArray[5] != null) ? ((Number) resultArray[5]).longValue() : 0L;
        }

        return new ProjectClassificationCount(outros, contratos, convenio, patrocinio, termoDeCooperacao, termoDeOutorga);
    }

    public ProjectMonthCount countProjectsByMonthForCompany() {
        List<Object[]> results = projectRepository.countProjectsByMonthForCompany();

        Long janeiro = 0L, fevereiro = 0L, marco = 0L, abril = 0L, maio = 0L, junho = 0L;
        Long julho = 0L, agosto = 0L, setembro = 0L, outubro = 0L, novembro = 0L, dezembro = 0L;

        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];

            if (month == null) {
                continue;
            }

            switch (month) {
                case 1: janeiro = count; break;
                case 2: fevereiro = count; break;
                case 3: marco = count; break;
                case 4: abril = count; break;
                case 5: maio = count; break;
                case 6: junho = count; break;
                case 7: julho = count; break;
                case 8: agosto = count; break;
                case 9: setembro = count; break;
                case 10: outubro = count; break;
                case 11: novembro = count; break;
                case 12: dezembro = count; break;
            }
        }
        return new ProjectMonthCount(janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro);
    }

    public ProjectInvestment calculateInvestmentByCompany(String companyName) {
        System.out.println("Company name received: " + companyName);
        Long totalInvestment = projectRepository.calculateTotalInvestmentByCompany(companyName);
        System.out.println("Total investment calculated: " + totalInvestment);
        return new ProjectInvestment(totalInvestment);
    }
}

