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
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectStatusCount countProjectsByStatus(String coordinatorName, String startDate, String endDate) {
        try {
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
        } catch (Exception e) {
            return new ProjectStatusCount(0L, 0L, 0L);
        }
    }

    public ProjectClassificationCount countProjectsByClassification(String coordinatorName, String startDate, String endDate) {
        try{
        LocalDate projectStartDate = null;
        LocalDate projectEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            projectStartDate = YearMonth.parse(startDate).atDay(1);
        }

        if (endDate != null && !endDate.isEmpty()) {
            projectEndDate = YearMonth.parse(endDate).atEndOfMonth();
        }
        List<Object[]> resultList = projectRepository.countProjectsByClassificationCoordinator(
                coordinatorName, projectStartDate, projectEndDate
        );

        Long outros = 0L;
        Long contratos = 0L;
        Long convenio = 0L;
        Long patrocinio = 0L;
        Long termoDeCooperacao = 0L;
        Long termoDeOutorga = 0L;

        if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 6) {
            Object[] resultArray = resultList.get(0);
            outros = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
            contratos = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            convenio = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            patrocinio = (resultArray[3] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            termoDeCooperacao = (resultArray[4] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            termoDeOutorga = (resultArray[5] != null) ? ((Number) resultArray[2]).longValue() : 0L;
        }

            return new ProjectClassificationCount(outros, contratos, convenio, patrocinio, termoDeCooperacao, termoDeOutorga);
        } catch (Exception e) {
            return new ProjectClassificationCount(0L, 0L, 0L, 0L, 0L, 0L);
        }
    }

    public ProjectMonthCount countProjectsByMonth(String coordinatorName, String startDate, String endDate) {
        try{
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
                case 1:
                    janeiro = count;
                    break;
                case 2:
                    fevereiro = count;
                    break;
                case 3:
                    marco = count;
                    break;
                case 4:
                    abril = count;
                    break;
                case 5:
                    maio = count;
                    break;
                case 6:
                    junho = count;
                    break;
                case 7:
                    julho = count;
                    break;
                case 8:
                    agosto = count;
                    break;
                case 9:
                    setembro = count;
                    break;
                case 10:
                    outubro = count;
                    break;
                case 11:
                    novembro = count;
                    break;
                case 12:
                    dezembro = count;
                    break;
            }
        }
            return new ProjectMonthCount(janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro);
        } catch (Exception e) {
            return new ProjectMonthCount(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        }
    }

    public ProjectStatusCount countProjectsByStatusForCompany(String companyName, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);


            List<Object[]> resultList = projectRepository.countProjectsByStatusForCompany(projectStartDate, projectEndDate, companyName);

            Long naoIniciados = 0L, emAndamento = 0L, finalizados = 0L;
            if (resultList != null && !resultList.isEmpty() && resultList.get(0).length == 3) {
                Object[] resultArray = resultList.get(0);
                naoIniciados = (resultArray[0] != null) ? ((Number) resultArray[0]).longValue() : 0L;
                emAndamento = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
                finalizados = (resultArray[2] != null) ? ((Number) resultArray[2]).longValue() : 0L;
            }
            return new ProjectStatusCount(naoIniciados, emAndamento, finalizados);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Erro no formato de data. Use o formato 'yyyy-MM'.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar projetos por status para a empresa.", e);
        }
    }

    public ProjectClassificationCount countProjectsByClassificationForCompany(String companyName, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            List<Object[]> resultList = projectRepository.countProjectsByClassificationForCompany(companyName, projectStartDate, projectEndDate);

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
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Erro no formato de data. Use o formato 'yyyy-MM'.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar projetos por classificação para a empresa.", e);
        }
    }

    public ProjectMonthCount countProjectsByMonthForCompany(String companyName, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            List<Object[]> results = projectRepository.countProjectsByMonthForCompany(companyName, projectStartDate, projectEndDate);

            Long janeiro = 0L, fevereiro = 0L, marco = 0L, abril = 0L, maio = 0L, junho = 0L;
            Long julho = 0L, agosto = 0L, setembro = 0L, outubro = 0L, novembro = 0L, dezembro = 0L;

            for (Object[] result : results) {
                Integer month = (Integer) result[0];
                Long count = (Long) result[1];
                if (month == null) continue;

                switch (month) {
                    case 1 -> janeiro = count;
                    case 2 -> fevereiro = count;
                    case 3 -> marco = count;
                    case 4 -> abril = count;
                    case 5 -> maio = count;
                    case 6 -> junho = count;
                    case 7 -> julho = count;
                    case 8 -> agosto = count;
                    case 9 -> setembro = count;
                    case 10 -> outubro = count;
                    case 11 -> novembro = count;
                    case 12 -> dezembro = count;
                }
            }
            return new ProjectMonthCount(janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Erro no formato de data. Use o formato 'yyyy-MM'.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar projetos por mês para a empresa.", e);
        }
    }

    public ProjectInvestment calculateInvestmentByCompany(String companyName, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            Long totalInvestment = projectRepository.calculateTotalInvestmentByCompany(companyName, projectStartDate, projectEndDate);
            return new ProjectInvestment(totalInvestment);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Erro no formato de data. Use o formato 'yyyy-MM'.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular o investimento total para a empresa.", e);
        }
    }

    private LocalDate parseDate(String date, boolean startOfMonth) {
        try {
            if (date != null && !date.isEmpty()) {
                date = date.trim();
                YearMonth yearMonth = YearMonth.parse(date);
                return startOfMonth ? yearMonth.atDay(1) : yearMonth.atEndOfMonth();
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato 'yyyy-MM'.", e);
        }
        return null;
    }
}

