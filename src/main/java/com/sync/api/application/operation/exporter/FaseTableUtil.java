package com.sync.api.application.operation.exporter;

import com.sync.api.domain.model.Phases;
import com.sync.api.domain.model.Plan;
import com.sync.api.domain.model.Schedule;
import com.sync.api.domain.model.Team;
import org.apache.poi.xwpf.usermodel.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class FaseTableUtil {

	void substituirFases(XWPFTable table, List<Phases> fases) {
		// Índice para numerar as fases
		int index = 1;

		for (Phases fase : fases) {
			// Criar uma nova linha na tabela para cada fase
			XWPFTableRow newRow = table.createRow();

			// Preencher a coluna do número da fase
			newRow.getCell(0).setText(String.valueOf(index));

			// Preencher a coluna da fase
			newRow.getCell(1).setText(fase.getPhase());

			// Preencher a coluna da descrição
			newRow.getCell(2).setText(fase.getDescription());

			// Incrementar o índice
			index++;
		}
	}


	void substituirCronograma(XWPFTable table, List<Schedule> cronograma) {
		int index = 1;
		for (Schedule atividade : cronograma) {
			XWPFTableRow newRow = table.createRow();
			newRow.getCell(0).setText(String.valueOf(index));
			newRow.getCell(1).setText(atividade.getActivity());
			newRow.getCell(2).setText(atividade.getPhysicalIndicator());
			newRow.getCell(3).setText(atividade.getStartDate());
			if (newRow.getCell(4) == null) {
				newRow.addNewTableCell();
			}
			newRow.getCell(4).setText(atividade.getEndDate());
			index++;
		}
	}

	void substituirEquipe(XWPFTable table, List<Team> equipe) {
		int index = 1;
		for (Team membro : equipe) {
			XWPFTableRow newRow = table.createRow();

			newRow.getCell(0).setText(String.valueOf(index));
			newRow.getCell(1).setText(membro.getName());
			newRow.getCell(2).setText(membro.getInstitution());
			newRow.getCell(3).setText(membro.getCompetence());

			index++;
		}
	}

	void substituirPlanoAplicacao(XWPFTable table, List<Plan> planoAplicacao) {
		int index = 1;
		for (Plan item : planoAplicacao) {
			XWPFTableRow newRow = table.createRow();

			newRow.getCell(0).setText(String.valueOf(index));
			newRow.getCell(1).setText(item.getDescription());
			newRow.getCell(2).setText(item.getTotal());

			index++;
		}
	}

	void substituirTotalPlanoAplicacao(XWPFTable table, List<Plan> planoAplicacao) {
		double total = calcularTotalPlanoAplicacao(planoAplicacao);
		String totalStr = String.format("%.2f", total);

		for (XWPFTableRow row : table.getRows()) {
			for (XWPFTableCell cell : row.getTableCells()) {
				for (XWPFParagraph paragraph : cell.getParagraphs()) {
					for (XWPFRun run : paragraph.getRuns()) {
						String text = run.getText(0);
						if (text != null && text.contains("{{plano_aplicacao_total}}")) {
							text = text.replace("{{plano_aplicacao_total}}", totalStr);
							run.setText(text, 0);
						}
					}
				}
			}
		}
	}

	public static double calcularTotalPlanoAplicacao(List<Plan> planoAplicacao) {
		return planoAplicacao.stream()
				.mapToDouble(item -> {
					try {
						return Double.parseDouble(item.getTotal());
					} catch (NumberFormatException e) {
						return 0.0;
					}
				})
				.sum();
	}

	void substituirCronogramaFinanceiro(XWPFTable table, List<String> cronogramaFinanceiro, String projectStartDate) {
		// Converter a data de início do projeto para obter o ano e o mês
		LocalDate dataInicio = LocalDate.parse(projectStartDate); // Certifique-se de que o formato da data seja ISO (yyyy-MM-dd)
		int mesInicial = dataInicio.getMonthValue(); // Mês de início
		int anoInicial = dataInicio.getYear();      // Ano de início

		// Índice para numerar as parcelas
		int numeroParcela = 1;

		for (String parcela : cronogramaFinanceiro) {
			// Criar uma nova linha na tabela para cada parcela
			XWPFTableRow newRow = table.createRow();

			// Preencher o número da parcela


			// Calcular o mês e o ano da parcela
			int mesAtual = (mesInicial + numeroParcela - 1) % 12;
			int anoAtual = anoInicial + (mesInicial + numeroParcela - 1) / 12;
			String mesAno = String.format("%02d/%d", mesAtual == 0 ? 12 : mesAtual, mesAtual == 0 ? anoAtual - 1 : anoAtual);

			// Preencher o mês/ano
			newRow.getCell(0).setText(mesAno);

			newRow.getCell(1).setText(String.valueOf(numeroParcela));

			// Preencher o valor da parcela
			newRow.getCell(2).setText(parcela);

			// Incrementar o índice da parcela
			numeroParcela++;
		}
	}


	void removerPlaceholdersRestantes(XWPFDocument document) {
		// Remover placeholders dos parágrafos
		for (XWPFParagraph paragraph : document.getParagraphs()) {
			for (XWPFRun run : paragraph.getRuns()) {
				String text = run.getText(0);
				if (text != null && text.matches("\\{\\{.*?}}")) { // Verifica se é um placeholder
					run.setText("", 0); // Remove o texto
				}
			}
		}

		// Remover placeholders das tabelas
		for (XWPFTable table : document.getTables()) {
			for (XWPFTableRow row : table.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph paragraph : cell.getParagraphs()) {
						for (XWPFRun run : paragraph.getRuns()) {
							String text = run.getText(0);
							if (text != null && text.matches("\\{\\{.*?}}")) { // Verifica se é um placeholder
								run.setText("", 0); // Remove o texto
							}
						}
					}
				}
			}
		}
	}

	public static String formatarDataPorExtenso(String dataISO) {
		if (dataISO == null || dataISO.isEmpty()) {
			return "";
		}
		LocalDate data = LocalDate.parse(dataISO); // A data deve estar no formato ISO: yyyy-MM-dd
		String dia = String.valueOf(data.getDayOfMonth());
		String mes = data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")); // Nome do mês por extenso
		String ano = String.valueOf(data.getYear());
		return dia + " de " + capitalizeFirstLetter(mes) + " de " + ano;
	}

	private static String capitalizeFirstLetter(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}


}
