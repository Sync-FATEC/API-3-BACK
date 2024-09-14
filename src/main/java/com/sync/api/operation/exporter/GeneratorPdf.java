package com.sync.api.operation.exporter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sync.api.model.Project;
import com.sync.api.operation.contract.Exporter;

import java.io.ByteArrayOutputStream;

public class GeneratorPdf implements Exporter {
    @Override
    public byte[] export(Project project) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Referência do projeto: " + project.getProjectReference()));
            document.add(new Paragraph("Empresa: " + project.getProjectCompany()));
            document.add(new Paragraph("Objetivo: " + (project.getProjectObjective() != null ? project.getProjectObjective() : "Não Informado")));
            document.add(new Paragraph("Descrição: " + (project.getProjectDescription() != null ? project.getProjectDescription() : "Não Informado")));
            document.add(new Paragraph("Coordenador: " + project.getNameCoordinator()));
            document.add(new Paragraph("Valor do projeto: " + project.getProjectValue()));
            document.add(new Paragraph("Data de início: " + project.getProjectStartDate()));
            document.add(new Paragraph("Data de término: " + project.getProjectEndDate()));
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}

