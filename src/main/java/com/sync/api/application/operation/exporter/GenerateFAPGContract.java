package com.sync.api.application.operation.exporter;

import com.sync.api.application.operation.StringExtensions;
import com.sync.api.domain.model.Address;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.WorkPlanCompleteData;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenerateFAPGContract {

    public byte[] generateContract(Project project) {
        var data = project.getWorkPlan();
        var contractorAddress = project.getCompany().getAddress();

        if (contractorAddress == null) {
            contractorAddress = new Address();
        }

        var coordinator = project.getCoordinators();

        try (InputStream inputStream = getClass().getResourceAsStream("/templates/Contrato_FAPG_template_template.docx")) {
            assert inputStream != null;
            try (XWPFDocument document = new XWPFDocument(inputStream);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                // Substituir texto em parágrafos
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    replaceTextInParagraph(paragraph, data, contractorAddress, coordinator, project);
                }

                // Substituir texto em tabelas
                for (XWPFTable table : document.getTables()) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                replaceTextInParagraph(paragraph, data, contractorAddress, coordinator, project);
                            }
                        }
                    }
                }

                document.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, WorkPlanCompleteData data, Address contractorAddress,
                                        Coordinators coordinator, Project project) {
        String fullText = paragraph.getText();

        if (fullText.contains("{{")) {
            // Substituições de variáveis no texto consolidado
            fullText = fullText.replace("{{project_referencia}}", getValueOrDefault(data.getProjectReference()))
                    .replace("{{contractor_name}}", getValueOrDefault(data.getCompanyRazaoSocial()))
                    .replace("{{contractor_address_street}}", getValueOrDefault(contractorAddress.getStreet()))
                    .replace("{{contractor_bairro}}", getValueOrDefault(contractorAddress.getNeighborhood()))
                    .replace("{{contractor_city}}", getValueOrDefault(contractorAddress.getCity()))
                    .replace("{{contractor_state}}", getValueOrDefault(contractorAddress.getState()))
                    .replace("{{contractor_cep}}", getValueOrDefault(contractorAddress.getZipCode()))
                    .replace("{{company_cnpj}}", getValueOrDefault(data.getCompanyCNPJ()))
                    .replace("{{coordinator_name}}", getValueOrDefault(data.getCoordinatorName()))
                    .replace("{{coordinator_nationality}}", getValueOrDefault(coordinator.getCoordinatorNacionality()))
                    .replace("{{coordinator_estado_civil}}", getValueOrDefault(coordinator.getCoordinatorMaritalStatus()))
                    .replace("{{coordinator_economic_activity}}", getValueOrDefault(data.getCoordinatorEconomicActivity()))
                    .replace("{{coordinator_cpf}}", getValueOrDefault(data.getCoordinatorCPF()))
                    .replace("{{coordinator_rg}}", getValueOrDefault(coordinator.getCoordinatorRG()))
                    .replace("{{coordinator_address}}", getValueOrDefault(data.getCoordinatorAddress()))
                    .replace("{{coordinator_cep}}", getValueOrDefault(data.getCoordinatorCEP()))
                    .replace("{{coordinator_city}}", getValueOrDefault(data.getCoordinatorCity()))
                    .replace("{{coordinator_uf}}", getValueOrDefault(data.getCoordinatorUF()))
                    .replace("{{coordinator_period}}", getValueOrDefault(data.getCoordinatorPeriod()))
                    .replace("{{coordinator_period_extense}}", getValueOrDefault(StringExtensions.toExtenso(data.getCoordinatorPeriod())))
                    .replace("{{project_value}}", getValueOrDefault(project.getProjectValue() != null ? project.getProjectValue().toString() : ""))
                    .replace("{{project_value_extense}}", getValueOrDefault(project.getProjectValue() != null ? StringExtensions.toExtenso(project.getProjectValue().toString()) : ""))
                    .replace("{{data_assinatura}}", getValueOrDefault(data.getDataAssinatura() != null ? FaseTableUtil.formatarDataPorExtenso(data.getDataAssinatura()) : ""))
                    .replace("{{contratante_nome}}", getValueOrDefault(data.getContratanteNome()))
                    .replace("{{contratante_cargo}}", getValueOrDefault(data.getContratanteCargo()));

            // Remove os runs existentes
            paragraph.getRuns().forEach(run -> run.setText("", 0));

            // Adiciona o texto modificado em um único run
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(fullText);
        }
    }

    private String getValueOrDefault(String value) {
        return value != null && !value.isEmpty() ? value : "Dado nao preenchido";
    }
}
