package com.sync.api.application.operation.exporter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sync.api.domain.model.Project;
import com.sync.api.application.operation.contract.Exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneratorPdf implements Exporter {
    @Override
    public byte[] export(Project project) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Adiciona uma imagem do caminho local
            String imagePath = "src/main/resources/images/logo.jpg"; // Altere para o caminho correto da sua imagem
            Image logo = Image.getInstance(imagePath);
            logo.scaleToFit(140, 120);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph(" ")); // Espaço em branco

            // Título do documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Paragraph title = new Paragraph("Portal Transparência - FAPG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Espaço em branco

            // Subtítulo
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph subtitle = new Paragraph("Informações do Projeto", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(new Paragraph(" ")); // Espaço em branco

            // Fontes para os rótulos e valores
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Informações do projeto
            document.add(new Paragraph("Referência do projeto:", subTitleFont));
            document.add(new Paragraph(project.getProjectReference(), normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Empresa:", subTitleFont));
            document.add(new Paragraph(project.getProjectCompany(), normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Objetivo:", subTitleFont));
            document.add(new Paragraph(project.getProjectObjective() != null ? project.getProjectObjective() : "Não Informado", normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Descrição:", subTitleFont));
            document.add(new Paragraph(project.getProjectDescription() != null ? project.getProjectDescription() : "Não Informado", normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Coordenador:", subTitleFont));
            document.add(new Paragraph(String.valueOf(project.getCoordinators().coordinatorName), normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Valor do projeto:", subTitleFont));
            document.add(new Paragraph(String.valueOf(project.getProjectValue()), normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Data de início:", subTitleFont));
            document.add(new Paragraph(project.getProjectStartDate().toString(), normalFont));
            document.add(new Paragraph(" ")); // Espaço em branco

            document.add(new Paragraph("Data de término:", subTitleFont));
            document.add(new Paragraph(project.getProjectEndDate().toString(), normalFont));

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generating PDF: " + e.toString(), e); // Use e.toString() em vez de e.getMessage()
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}
