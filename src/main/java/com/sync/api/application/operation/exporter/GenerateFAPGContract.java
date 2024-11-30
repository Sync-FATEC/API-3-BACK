package com.sync.api.application.operation.exporter;

import com.sync.api.application.operation.StringExtensions;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.WorkPlanCompleteData;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenerateFAPGContract {

    public byte[] generateContract(Project project){

        var data = project.getWorkPlan();
        var contractorAddress = project.getCompany().getAddress();

        var coordinator = project.getCoordinators();


        try (InputStream inputStream = getClass().getResourceAsStream("/templates/Contrato_FAPG_template_template.doc")) {
            assert inputStream != null;
            try (XWPFDocument document = new XWPFDocument(inputStream);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                for (XWPFHeader header : document.getHeaderList()) {
                    for (XWPFParagraph paragraph : header.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            String text = run.getText(0);
                            if (text != null) {
                                text = text.replace("{{project_referencia}}", getValueOrDefault(data.getProjectReference()))
                                        .replace("{{contractor_name}}", getValueOrDefault(data.getCompanyRazaoSocial()));
                                run.setText(text, 0);
                            }
                        }
                    }
                }
                for (XWPFTable table : document.getTables()) {
                    List<XWPFTableRow> rows = new ArrayList<>(table.getRows());
                    for (XWPFTableRow row : rows) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                for (XWPFRun run : paragraph.getRuns()) {
                                    String text = run.getText(0);
                                    if (text != null) {
                                        text = text.replace("{{coordinator_name}}", getValueOrDefault(data.getCoordinatorName()))
                                                .replace("{{coordinator_nationality}}", getValueOrDefault(coordinator.getCoordinatorNacionality()))
                                                .replace("{{coordinator_estado_civil}}", getValueOrDefault(coordinator.getCoordinatorMaritalStatus()))
                                                .replace("{{coordinator_cpf}}", getValueOrDefault(data.getCoordinatorCPF()))
                                                .replace("{{coordinator_rg}}", getValueOrDefault(coordinator.getCoordinatorRG()))
                                                .replace("{{coordinator_address}}", getValueOrDefault(data.getCoordinatorAddress()))
                                                .replace("{{coordinator_city}}", getValueOrDefault(data.getCoordinatorCity()))
                                                .replace("{{coordinator_uf}}", getValueOrDefault(data.getCoordinatorUF()))
                                                .replace("{{coordinator_cep}}", getValueOrDefault(data.getCoordinatorCEP()))
                                                .replace("{{coordinator_phone}}", getValueOrDefault(data.getCoordinatorTelefone()))
                                                .replace("{{coordinator_economic_activity}}", getValueOrDefault(data.getCoordinatorEconomicActivity()))
                                                .replace("{{coordinator_period}}", getValueOrDefault(data.getCoordinatorPeriod()))
                                                .replace("{{coordinator_period_extense}}", getValueOrDefault(StringExtensions.toExtenso(data.getCoordinatorPeriod())))
                                                .replace("{{company_name}}", getValueOrDefault(data.getCompanyRazaoSocial()))
                                                .replace("{{company_cnpj}}", getValueOrDefault(data.getCompanyCNPJ()))
                                                .replace("{{company_responsavel_tecnico}}", getValueOrDefault(data.getCompanyResponsavelTecnico()))
                                                .replace("{{company_phone}}", getValueOrDefault(data.getCompanyTelefone()))
                                                .replace("{{contractor_address_street}}", getValueOrDefault(contractorAddress.getStreet()))
                                                .replace("{{contractor_bairro}}", getValueOrDefault(contractorAddress.getNeighborhood()))
                                                .replace("{{contractor_city}}", getValueOrDefault(contractorAddress.getCity()))
                                                .replace("{{contractor_state}}", getValueOrDefault(contractorAddress.getState()))
                                                .replace("{{contractor_cep}}", getValueOrDefault(contractorAddress.getZipCode()))
                                                .replace("{{project_value}}", getValueOrDefault(project.getProjectValue() != null ? project.getProjectValue().toString() : ""))
                                                .replace("{{project_value_extense}}", getValueOrDefault(project.getProjectValue() != null ? StringExtensions.toExtenso(project.getProjectValue().toString()) : ""))
                                                .replace("{{data_assinatura}}", getValueOrDefault(data.getDataAssinatura() != null ? String.format(data.getDataAssinatura()) : ""))
                                                .replace("{{company_empresa_privada}}", getValueOrDefault(data.getCompanyEmpresaPrivada()))
                                                .replace("{{project_title}}", getValueOrDefault(data.getProjectTitle()))
                                                .replace("{{project_start_date}}", getValueOrDefault(data.getProjectStartDate() != null ? data.getProjectStartDate().format(DateTimeFormatter.ISO_DATE) : ""))
                                                .replace("{{project_objective}}", getValueOrDefault(data.getProjectObjective()))
                                                .replace("{{coordinator_name}}", getValueOrDefault(data.getCoordinatorName()))
                                                .replace("{{coordinator_periodo}}", getValueOrDefault(data.getCoordinatorPeriod()));
                                        run.setText(text, 0);
                                    }
                                }
                            }
                        }
                    }
                }

                // Substituir a data da assinatura
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        String text = run.getText(0);
                        if (text != null && text.contains("{{data_assinatura}}")) {
                            text = text.replace("{{data_assinatura}}",
                                    FaseTableUtil.formatarDataPorExtenso(data.getDataAssinatura()));
                            run.setText(text, 0);
                        }
                    }
                }


                // Substituir o nome e cargo do contratante
                for (XWPFTable table : document.getTables()) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                for (XWPFRun run : paragraph.getRuns()) {
                                    String text = run.getText(0);
                                    if (text != null) {
                                        text = text.replace("{{contratante_nome}}", data.getContratanteNome() != null ? data.getContratanteNome() : "")
                                                .replace("{{contratante_cargo}}", data.getContratanteCargo() != null ? data.getContratanteCargo() : "");
                                        run.setText(text, 0);
                                    }
                                }
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


    private String getValueOrDefault(String value) {
        return value != null && !value.isEmpty() ? value : "Dado nao preenchido";
    }
}
