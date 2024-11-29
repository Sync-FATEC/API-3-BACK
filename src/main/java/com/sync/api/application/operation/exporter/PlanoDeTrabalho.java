package com.sync.api.application.operation.exporter;

import com.sync.api.domain.model.WorkPlanCompleteData;
import com.sync.api.web.dto.workplan.CronogramaDTO;
import com.sync.api.web.dto.workplan.EquipeDTO;
import com.sync.api.web.dto.workplan.FaseDTO;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.text;
import static kotlin.text.Typography.paragraph;

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
							            .replace("{{company_name}}", data.getCompanyRazaoSocial() != null ? data.getCompanyRazaoSocial() : "");
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
							            // Substituir todos os placeholders disponíveis
							            text = text.replace("{{coordinator_name}}", data.getCoordinatorName() != null ? data.getCoordinatorName() : "")
									            .replace("{{coordinator_cpf}}", data.getCoordinatorCPF() != null ? data.getCoordinatorCPF() : "")
									            .replace("{{coordinator_address}}", data.getCoordinatorAddress() != null ? data.getCoordinatorAddress() : "")
									            .replace("{{coordinator_city}}", data.getCoordinatorCity() != null ? data.getCoordinatorCity() : "")
									            .replace("{{coordinator_uf}}", data.getCoordinatorUF() != null ? data.getCoordinatorUF() : "")
									            .replace("{{coordinator_cep}}", data.getCoordinatorCEP() != null ? data.getCoordinatorCEP() : "")
									            .replace("{{coordinator_phone}}", data.getCoordinatorTelefone() != null ? data.getCoordinatorTelefone() : "")
									            .replace("{{coordinator_economic_activity}}", data.getCoordinatorEconomicActivity() != null ? data.getCoordinatorEconomicActivity() : "")
									            .replace("{{company_name}}", data.getCompanyRazaoSocial() != null ?
											            data.getCompanyRazaoSocial() : "")
									            .replace("{{company_cnpj}}", data.getCompanyCNPJ() != null ? data.getCompanyCNPJ() : "")
									            .replace("{{company_responsavel_tecnico}}", data.getCompanyResponsavelTecnico() != null ? data.getCompanyResponsavelTecnico() : "")
									            .replace("{{company_phone}}", data.getCompanyTelefone() != null ? data.getCompanyTelefone() : "")
									            .replace("{{company_address}}", data.getCompanyEndereco() != null ? data.getCompanyEndereco() : "")
									            .replace("{{company_empresa_privada}}", data.getCompanyEmpresaPrivada() != null ? data.getCompanyEmpresaPrivada() : "")
									            .replace("{{project_title}}", data.getProjectTitle() != null ? data.getProjectTitle() : "")
									            .replace("{{project_start_date}}", data.getProjectStartDate() != null ? data.getProjectStartDate().format(DateTimeFormatter.ISO_DATE) : "")
									            .replace("{{project_objective}}", data.getProjectObjective() != null ? data.getProjectObjective() : "")
									            .replace("{{project_justificativa}}",
											            data.getProjetoJustificativa() != null ? data.getProjetoJustificativa() : "")
									            .replace("{{project_resultados_esperados}}",
											            data.getProjetoResultadosEsperados() != null ? data.getProjetoResultadosEsperados() : "")
									            .replace("{{coordinator_name}}", data.getCoordinatorName() != null ? data.getCoordinatorName() : "")
									            .replace("{{coordinator_periodo}}", data.getCoordinatorPeriod() != null ? data.getCoordinatorPeriod() : "");
							            run.setText(text, 0);
						            }
					            }
				            }
			            }
		            }
	            }
	            FaseTableUtil faseTableUtil = new FaseTableUtil();
	            for (XWPFTable table : document.getTables()) {
		            List<XWPFTableRow> rows = new ArrayList<>(table.getRows());
		            for (XWPFTableRow row : rows) {
			            for (XWPFTableCell cell : row.getTableCells()) {
				            for (XWPFParagraph paragraph : cell.getParagraphs()) {
					            for (XWPFRun run : paragraph.getRuns()) {
						            String text = run.getText(0);
						            if (text != null) {
							            if (text.contains("{{fases_entregas_inicio}}")) {
								            faseTableUtil.substituirFases(table, data.getPhases());
							            }
							            if (text.contains("{{inicio_cronograma}}")) {
								            faseTableUtil.substituirCronograma(table, data.getSchedules());
							            }
							            if (text.contains("{{inicio_equipe_executora}}")) {
								            faseTableUtil.substituirEquipe(table, data.getTeams());
							            }
							            if (text.contains("{{inicio_plano_aplicacao}}")) {
                                           faseTableUtil.substituirPlanoAplicacao(table, data.getApplicationPlans());
                                       }
							            if (text.contains("{{inicio_cronograma_financeiro}}")) {
								            faseTableUtil.substituirCronogramaFinanceiro(table,
                                                   data.getCronogramaFinanceiro(), data.getProjectStartDate().format(DateTimeFormatter.ISO_DATE));
							            }
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


	            // Substituir o valor total do plano de aplicação
               for (XWPFTable table : document.getTables()) {
                   faseTableUtil.substituirTotalPlanoAplicacao(table, data.getApplicationPlans());
               }

	            // Escrever o documento
	            faseTableUtil.removerPlaceholdersRestantes(document);
	            document.write(outputStream);
	            return outputStream.toByteArray();
           }
       }
   }
}