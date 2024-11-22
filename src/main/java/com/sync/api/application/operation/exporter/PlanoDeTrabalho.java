package com.sync.api.application.operation.exporter;

import com.sync.api.domain.model.WorkPlanCompleteData;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PlanoDeTrabalho {

	public byte[] gerarPlanoDeTrabalho(WorkPlanCompleteData data) throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("/templates/Plano_de_Trabalho_FAPG_template_variables.docx")) {
			assert inputStream != null;
			try (XWPFDocument document = new XWPFDocument(inputStream);
			     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

				// Substituir placeholders no documento
				for (XWPFHeader header : document.getHeaderList()) {
					for (XWPFParagraph paragraph : header.getParagraphs()) {
						for (XWPFRun run : paragraph.getRuns()) {
							String text = run.getText(0);
							if (text != null) {
								text = text.replace("{{project_referencia}}", data.getProjectReference() != null ? data.getProjectReference() : "")
										.replace("{{company_name}}", data.getCompanyName() != null ? data.getCompanyName() : "");
								run.setText(text, 0);
							}
						}
					}
				}
				for (XWPFTable table : document.getTables()) {
					for (XWPFTableRow row : table.getRows()) {
						for (XWPFTableCell cell : row.getTableCells()) {
							for (XWPFParagraph paragraph : cell.getParagraphs()) {
								for (XWPFRun run : paragraph.getRuns()) {
									String text = run.getText(0);
									if (text != null) {
										// Substituir todos os placeholders disponíveis
										text = text.replace("{{coordinator_name}}", data.getCoordinatorName() != null ? data.getCoordinatorName() : "")
												.replace("{{project_title}}", data.getProjectTitle() != null ? data.getProjectTitle() : "")
												.replace("{{company_name}}", data.getCompanyName() != null ?
														data.getCompanyName() : "")
												.replace("{{coordinator_cpf}}", data.getCoordinatorCpf() != null ? data.getCoordinatorCpf() : "")
												.replace("{{coordinator_phone}}",
														data.getCoordinatorTelefone() != null ?
														data.getCoordinatorTelefone() : "")
												.replace("{{company_phone}}", data.getCompanyPhone() != null ? data.getCompanyPhone() : "")
												.replace("{{coordinator_economic_activity}}", data.getCoordinatorEconomicActivity() != null ? data.getCoordinatorEconomicActivity() : "")
												.replace("{{company_cnpj}}", data.getCnpj() != null ? data.getCnpj() : "")
												.replace("{{project_start_date}}", data.getProjectStartDate() != null ? data.getProjectStartDate() : "")
												.replace("{{project_objective}}", data.getProjectObjective() != null ? data.getProjectObjective() : "");
										run.setText(text, 0);
									}
								}
							}
						}
					}
				}

				// Escrever o documento
				document.write(outputStream);
				return outputStream.toByteArray();
			}
		}
	}
}
