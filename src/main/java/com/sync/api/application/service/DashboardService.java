package com.sync.api.application.service;

import com.itextpdf.text.*;
import com.sync.api.web.dto.project.Dashboard.ProjectClassificationCount;
import com.sync.api.web.dto.project.Dashboard.ProjectInvestment;
import com.sync.api.web.dto.project.Dashboard.ProjectMonthCount;
import com.sync.api.web.dto.project.Dashboard.ProjectStatusCount;
import com.sync.api.infra.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

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
            contratos = (resultArray[1] != null) ? ((Number) resultArray[1]).longValue() : 0L;
            patrocinio = (resultArray[3] != null) ? ((Number) resultArray[2]).longValue() : 0L;
        }

            return new ProjectClassificationCount(contratos, patrocinio);
        } catch (Exception e) {
            return new ProjectClassificationCount(0L, 0L);
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

    public ProjectStatusCount countProjectsByStatusForCompany(String projectCompany, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);


            List<Object[]> resultList = projectRepository.countProjectsByStatusForCompany(projectStartDate, projectEndDate, projectCompany);

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

    public ProjectClassificationCount countProjectsByClassificationForCompany(String projectCompany, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            List<Object[]> resultList = projectRepository.countProjectsByClassificationForCompany(projectCompany, projectStartDate, projectEndDate);

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
            return new ProjectClassificationCount(contratos, patrocinio);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Erro no formato de data. Use o formato 'yyyy-MM'.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar projetos por classificação para a empresa.", e);
        }
    }

    public ProjectMonthCount countProjectsByMonthForCompany(String projectCompany, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            List<Object[]> results = projectRepository.countProjectsByMonthForCompany(projectCompany, projectStartDate, projectEndDate);

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

    public ProjectInvestment calculateInvestmentByCompany(String projectCompany, String startDate, String endDate) {
        try {
            LocalDate projectStartDate = parseDate(startDate, true);
            LocalDate projectEndDate = parseDate(endDate, false);

            Long totalInvestment = projectRepository.calculateTotalInvestmentByCompany(projectCompany, projectStartDate, projectEndDate);
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

    // Gera o PDF do Dashboard com base no Coordenador ou Empresa
    public byte[] exportDashboardToPDF(String nameCoordinator, String projectCompany, String startDate, String endDate) {
        return gerarRelatorioPDF(nameCoordinator, projectCompany, startDate, endDate);
    }

    // Gera o Excel do Dashboard com base no Coordenador ou Empresa
    public byte[] exportDashboardToExcel(String nameCoordinator, String projectCompany, String startDate, String endDate) {
        return gerarRelatorioExcel(nameCoordinator, projectCompany, startDate, endDate);
    }

    private byte[] gerarRelatorioPDF(String nameCoordinator, String projectCompany, String startDate, String endDate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            adicionarCabecalhoPDF(document);

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            String titleText = "";
            // Verifica se o relatório é para um coordenador ou empresa e ajusta o título
            if (nameCoordinator != null) {
                titleText += "Coordenador: " + nameCoordinator;
                document.add(new Paragraph(" ")); // Espaço em branco
            } else if (projectCompany != null) {
                titleText += "Empresa: " + projectCompany;
                document.add(new Paragraph(" ")); // Espaço em branco
            }
            Paragraph title = new Paragraph(titleText, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Espaço em branco

            // Adicionar informações de data se presentes
            if (startDate != null && endDate != null) {
                Paragraph dateInfo = new Paragraph("Período: De " + startDate + " até " + endDate, valueFont);
                dateInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(dateInfo);
            } else if (startDate != null) {
                Paragraph dateInfo = new Paragraph("Data de Início: " + startDate, valueFont);
                dateInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(dateInfo);
            } else if (endDate != null) {
                Paragraph dateInfo = new Paragraph("Data de Fim: " + endDate, valueFont);
                dateInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(dateInfo);
            }

            document.add(new Paragraph(" ")); // Espaço em branco

            // Determina o tipo de relatório com base nos parâmetros
            document.add(new Paragraph("Status dos Projetos", labelFont));
            ProjectStatusCount statusCount = (nameCoordinator != null)
                    ? countProjectsByStatus(nameCoordinator, startDate, endDate)
                    : countProjectsByStatusForCompany(projectCompany, startDate, endDate);
            adicionarTabelaStatusNoPDF(document, statusCount);
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Classificação dos Projetos", labelFont));
            ProjectClassificationCount classificationCount = (nameCoordinator != null)
                    ? countProjectsByClassification(nameCoordinator, startDate, endDate)
                    : countProjectsByClassificationForCompany(projectCompany, startDate, endDate);
            adicionarTabelaClassificacaoNoPDF(document, classificationCount);
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Distribuição de Projetos por Mês", labelFont));
            ProjectMonthCount monthCount = (nameCoordinator != null)
                    ? countProjectsByMonth(nameCoordinator, startDate, endDate)
                    : countProjectsByMonthForCompany(projectCompany, startDate, endDate);
            adicionarTabelaMesNoPDF(document, monthCount);
            document.add(new Paragraph(" ")); // Espaço em branco

            // Seção de Investimento Total (somente para empresas)
            if (projectCompany != null) {
                document.add(new Paragraph("Total de Investimento", labelFont));
                ProjectInvestment investment = calculateInvestmentByCompany(projectCompany, startDate, endDate);
                Paragraph investmentParagraph = new Paragraph("R$ " + investment.totalInvestment(), valueFont);
                investmentParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(investmentParagraph);
                document.add(new Paragraph(" ")); // Espaço em branco
            }

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erro ao gerar PDF do dashboard", e);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    private void adicionarCabecalhoPDF(Document document) throws DocumentException, IOException {
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph title = new Paragraph("Dashboard - Relatório de Projetos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Espaço em branco
        // Adiciona uma imagem (logo) centralizada no documento
        String imagePath = "src/main/resources/images/logo.jpg"; // Caminho para o logo, altere conforme necessário
        Image logo = Image.getInstance(imagePath);
        logo.scaleToFit(140, 120);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);
        document.add(new Paragraph(" ")); // Espaço em branco
    }

    private void adicionarTabelaStatusNoPDF(Document document, ProjectStatusCount statusCount) throws DocumentException {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);
        PdfPTable statusTable = new PdfPTable(2);
        statusTable.setWidthPercentage(100);
        statusTable.addCell(new PdfPCell(new Paragraph("Status", labelFont)));
        statusTable.addCell(new PdfPCell(new Paragraph("Contagem", labelFont)));
        statusTable.addCell(new PdfPCell(new Paragraph("Não Iniciado", valueFont)));
        statusTable.addCell(new PdfPCell(new Paragraph(String.valueOf(statusCount.naoIniciados()), valueFont)));
        statusTable.addCell(new PdfPCell(new Paragraph("Em Andamento", valueFont)));
        statusTable.addCell(new PdfPCell(new Paragraph(String.valueOf(statusCount.emAndamento()), valueFont)));
        statusTable.addCell(new PdfPCell(new Paragraph("Concluído", valueFont)));
        statusTable.addCell(new PdfPCell(new Paragraph(String.valueOf(statusCount.finalizados()), valueFont)));
        document.add(statusTable);
    }

    private void adicionarTabelaClassificacaoNoPDF(Document document, ProjectClassificationCount classificationCount) throws DocumentException {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);
        PdfPTable classificationTable = new PdfPTable(2);
        classificationTable.setWidthPercentage(100);
        classificationTable.addCell(new PdfPCell(new Paragraph("Classificação", labelFont)));
        classificationTable.addCell(new PdfPCell(new Paragraph("Contagem", labelFont)));
        classificationTable.addCell(new PdfPCell(new Paragraph("Contratos", valueFont)));
        classificationTable.addCell(new PdfPCell(new Paragraph(String.valueOf(classificationCount.contratos()), valueFont)));
        classificationTable.addCell(new PdfPCell(new Paragraph("Patrocínio", valueFont)));
        classificationTable.addCell(new PdfPCell(new Paragraph(String.valueOf(classificationCount.patrocinio()), valueFont)));
        document.add(classificationTable);
    }

    private void adicionarTabelaMesNoPDF(Document document, ProjectMonthCount monthCount) throws DocumentException {
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);
        PdfPTable monthTable = new PdfPTable(2);
        monthTable.setWidthPercentage(100);
        monthTable.addCell(new PdfPCell(new Paragraph("Mês", labelFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Contagem", labelFont)));

        // Preenchendo os dados dos meses
        monthTable.addCell(new PdfPCell(new Paragraph("Janeiro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.janeiro()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Fevereiro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.fevereiro()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Março", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.marco()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Abril", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.abril()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Maio", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.maio()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Junho", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.junho()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Julho", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.julho()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Agosto", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.agosto()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Setembro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.setembro()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Outubro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.outubro()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Novembro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.novembro()), valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph("Dezembro", valueFont)));
        monthTable.addCell(new PdfPCell(new Paragraph(String.valueOf(monthCount.dezembro()), valueFont)));

        document.add(monthTable);
    }

    private byte[] gerarRelatorioExcel(String nameCoordinator, String projectCompany, String startDate, String endDate) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Dados do Dashboard");

            int currentRow = 0;

            // Adicionar cabeçalho com o nome do coordenador ou empresa
            Row headerRow = sheet.createRow(currentRow++);
            if (nameCoordinator != null) {
                headerRow.createCell(0).setCellValue("Relatório do Coordenador:");
                headerRow.createCell(1).setCellValue(nameCoordinator);
            } else if (projectCompany != null) {
                headerRow.createCell(0).setCellValue("Relatório da Empresa:");
                headerRow.createCell(1).setCellValue(projectCompany);

                // Adicionar total de investimento para empresa
                ProjectInvestment investment = calculateInvestmentByCompany(projectCompany, startDate, endDate);
                Row investmentRow = sheet.createRow(currentRow++);
                investmentRow.createCell(0).setCellValue("Total de Investimento:");
                investmentRow.createCell(1).setCellValue("R$ " + investment.totalInvestment());
            }

            // Adicionar informações de data se presentes
            if (startDate != null && endDate != null) {
                Row dateRow = sheet.createRow(currentRow++);
                dateRow.createCell(0).setCellValue("Período:");
                dateRow.createCell(1).setCellValue("De " + startDate + " até " + endDate);
            } else if (startDate != null) {
                Row dateRow = sheet.createRow(currentRow++);
                dateRow.createCell(0).setCellValue("Data de Início:");
                dateRow.createCell(1).setCellValue(startDate);
            } else if (endDate != null) {
                Row dateRow = sheet.createRow(currentRow++);
                dateRow.createCell(0).setCellValue("Data de Fim:");
                dateRow.createCell(1).setCellValue(endDate);
            }
            currentRow++;

            // Seção de Status
            ProjectStatusCount statusCount = (nameCoordinator != null)
                    ? countProjectsByStatus(nameCoordinator, startDate, endDate)
                    : countProjectsByStatusForCompany(projectCompany, startDate, endDate);
            currentRow = adicionarTabelaStatusNoExcel(sheet, statusCount, currentRow);

            // Seção de Classificação
            ProjectClassificationCount classificationCount = (nameCoordinator != null)
                    ? countProjectsByClassification(nameCoordinator, startDate, endDate)
                    : countProjectsByClassificationForCompany(projectCompany, startDate, endDate);
            currentRow = adicionarTabelaClassificacaoNoExcel(sheet, classificationCount, currentRow);

            // Seção de Projetos por Mês
            ProjectMonthCount monthCount = (nameCoordinator != null)
                    ? countProjectsByMonth(nameCoordinator, startDate, endDate)
                    : countProjectsByMonthForCompany(projectCompany, startDate, endDate);
            adicionarTabelaMesNoExcel(sheet, monthCount, currentRow);

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar Excel do dashboard", e);
        }
    }

    private int adicionarTabelaStatusNoExcel(Sheet sheet, ProjectStatusCount statusCount, int startRow) {
        Row statusHeader = sheet.createRow(startRow++);
        statusHeader.createCell(0).setCellValue("Status");
        statusHeader.createCell(1).setCellValue("Quantidade");

        Row statusRow = sheet.createRow(startRow++);
        statusRow.createCell(0).setCellValue("Não Iniciado");
        statusRow.createCell(1).setCellValue(statusCount.naoIniciados());

        Row inProgressRow = sheet.createRow(startRow++);
        inProgressRow.createCell(0).setCellValue("Em andamento");
        inProgressRow.createCell(1).setCellValue(statusCount.emAndamento());

        Row completedRow = sheet.createRow(startRow++);
        completedRow.createCell(0).setCellValue("Finalizados");
        completedRow.createCell(1).setCellValue(statusCount.finalizados());

        return startRow + 1;
    }

    private int adicionarTabelaClassificacaoNoExcel(Sheet sheet, ProjectClassificationCount classificationCount, int startRow) {
        Row classificationHeader = sheet.createRow(startRow++);
        classificationHeader.createCell(0).setCellValue("Classificação");
        classificationHeader.createCell(1).setCellValue("Quantidade");

        Row contratosRow = sheet.createRow(startRow++);
        contratosRow.createCell(0).setCellValue("Contratos");
        contratosRow.createCell(1).setCellValue(classificationCount.contratos());

        Row patrocinioRow = sheet.createRow(startRow++);
        patrocinioRow.createCell(0).setCellValue("Patrocínio");
        patrocinioRow.createCell(1).setCellValue(classificationCount.patrocinio());

        return startRow + 1;
    }

    private void adicionarTabelaMesNoExcel(Sheet sheet, ProjectMonthCount monthCount, int startRow) {
        Row monthHeader = sheet.createRow(startRow++);
        monthHeader.createCell(0).setCellValue("Mês");
        monthHeader.createCell(1).setCellValue("Quantidade");

        Row janeiroRow = sheet.createRow(startRow++);
        janeiroRow.createCell(0).setCellValue("Janeiro");
        janeiroRow.createCell(1).setCellValue(monthCount.janeiro());

        Row fevereiroRow = sheet.createRow(startRow++);
        fevereiroRow.createCell(0).setCellValue("Fevereiro");
        fevereiroRow.createCell(1).setCellValue(monthCount.fevereiro());

        Row marcoRow = sheet.createRow(startRow++);
        marcoRow.createCell(0).setCellValue("Março");
        marcoRow.createCell(1).setCellValue(monthCount.marco());

        Row abrilRow = sheet.createRow(startRow++);
        abrilRow.createCell(0).setCellValue("Abril");
        abrilRow.createCell(1).setCellValue(monthCount.abril());

        Row maioRow = sheet.createRow(startRow++);
        maioRow.createCell(0).setCellValue("Maio");
        maioRow.createCell(1).setCellValue(monthCount.maio());

        Row junhoRow = sheet.createRow(startRow++);
        junhoRow.createCell(0).setCellValue("Junho");
        junhoRow.createCell(1).setCellValue(monthCount.junho());

        Row julhoRow = sheet.createRow(startRow++);
        julhoRow.createCell(0).setCellValue("Julho");
        julhoRow.createCell(1).setCellValue(monthCount.julho());

        Row agostoRow = sheet.createRow(startRow++);
        agostoRow.createCell(0).setCellValue("Agosto");
        agostoRow.createCell(1).setCellValue(monthCount.agosto());

        Row setembroRow = sheet.createRow(startRow++);
        setembroRow.createCell(0).setCellValue("Setembro");
        setembroRow.createCell(1).setCellValue(monthCount.setembro());

        Row outubroRow = sheet.createRow(startRow++);
        outubroRow.createCell(0).setCellValue("Outubro");
        outubroRow.createCell(1).setCellValue(monthCount.outubro());

        Row novembroRow = sheet.createRow(startRow++);
        novembroRow.createCell(0).setCellValue("Novembro");
        novembroRow.createCell(1).setCellValue(monthCount.novembro());

        Row dezembroRow = sheet.createRow(startRow++);
        dezembroRow.createCell(0).setCellValue("Dezembro");
        dezembroRow.createCell(1).setCellValue(monthCount.dezembro());
    }

}

