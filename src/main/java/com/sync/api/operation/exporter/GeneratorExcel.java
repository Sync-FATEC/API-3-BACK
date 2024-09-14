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

        // Criar a primeira linha (cabeçalhos)
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Referência do projeto:");
        headerRow.createCell(1).setCellValue("Coordenador");
        headerRow.createCell(2).setCellValue("Descrição");
        headerRow.createCell(3).setCellValue("Empresa");
        headerRow.createCell(4).setCellValue("Objetivo");
        headerRow.createCell(5).setCellValue("Valor do projeto");
        headerRow.createCell(6).setCellValue("Data de início");
        headerRow.createCell(7).setCellValue("Data de término");

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

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            workbook.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro em gerar o arquivo: " + e.getMessage(), e);
        }
    }
}
