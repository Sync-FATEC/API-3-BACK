package com.sync.api.operation.exporter;

import com.sync.api.model.Project;
import com.sync.api.operation.contract.Exporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneratorExcel implements Exporter {
    @Override
    public byte[] export(Project project) {
        // Criar uma nova planilha do Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Projeto");

        // Criar um estilo para o cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Criar a primeira linha (cabeçalhos)
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Referência do projeto",
                "Coordenador",
                "Descrição",
                "Empresa",
                "Objetivo",
                "Valor do projeto",
                "Data de início",
                "Data de término"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 20 * 256); // Ajusta a largura da coluna
        }

        // Criar uma nova linha para os dados do projeto
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(project.getProjectReference());
        dataRow.createCell(1).setCellValue(project.getNameCoordinator());
        dataRow.createCell(2).setCellValue(project.getProjectDescription());
        dataRow.createCell(3).setCellValue(project.getProjectCompany());
        dataRow.createCell(4).setCellValue(project.getProjectObjective());
        dataRow.createCell(5).setCellValue(project.getProjectValue());
        dataRow.createCell(6).setCellValue(project.getProjectStartDate().toString());
        dataRow.createCell(7).setCellValue(project.getProjectEndDate().toString());

        // Criar um estilo para os dados
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true); // Ativa a quebra de linha
        dataStyle.setAlignment(HorizontalAlignment.LEFT);

        for (int i = 0; i < headers.length; i++) {
            dataRow.getCell(i).setCellStyle(dataStyle);
        }

        // Adiciona bordas a todas as células
        for (int rowIndex = 0; rowIndex <= 1; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            for (int colIndex = 0; colIndex < headers.length; colIndex++) {
                Cell cell = row.getCell(colIndex);
                cell.getCellStyle().setBorderBottom(BorderStyle.THIN);
                cell.getCellStyle().setBorderTop(BorderStyle.THIN);
                cell.getCellStyle().setBorderRight(BorderStyle.THIN);
                cell.getCellStyle().setBorderLeft(BorderStyle.THIN);
            }
        }

        // Finaliza a escrita do arquivo
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            workbook.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro em gerar o arquivo: " + e.getMessage(), e);
        }
    }
}
