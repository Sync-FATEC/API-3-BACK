package com.sync.api.web.controller;

import com.sync.api.application.operation.exporter.PlanoDeTrabalho;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.domain.model.WorkPlanCompleteData;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.web.dto.workplan.MinimalWorkPlanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sync.api.domain.model.Project;

@RestController
@RequestMapping("/plano-de-trabalho")
public class PlanoDeTrabalhoController {

	private static final Logger logger = LoggerFactory.getLogger(PlanoDeTrabalhoController.class);

	@Autowired
	private PlanoDeTrabalho planoDeTrabalho;

	@Autowired
	private ProjectRepository projectRepository;

	@PostMapping("/gerar")
	public ResponseEntity<byte[]> generateWorkPlan(@RequestBody MinimalWorkPlanRequest request) {
		try {
			logger.info("Recebendo requisição para gerar plano de trabalho...");
			logger.info("Dados recebidos: {}", request);

			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + request.getProjectId()));

			Coordinators coordinator = project.getCoordinators();
			if (coordinator == null) {
				throw new IllegalArgumentException("Coordenador não encontrado para o projeto com ID: " + request.getProjectId());
			}

			// Montar os dados completos com base no payload recebido
			WorkPlanCompleteData completeData = new WorkPlanCompleteData(
					request.getProjectId(),
					project.getProjectReference(),
					project.getProjectTitle(),
					project.getProjectStartDate(),
					project.getProjectObjective(),
					coordinator.getCoordinatorName(),
					request.getCoordinatorCPF(),
					request.getCoordinatorAddress(),
					request.getCoordinatorCity(),
					request.getCoordinatorUF(),
					request.getCoordinatorCEP(),
					request.getCoordinatorTelefone(),
					request.getCoordinatorEconomicActivity(),
					request.getCoordinatorPeriod(),
					request.getCompanyRazaoSocial(),
					request.getCompanyCNPJ(),
					request.getCompanyResponsavelTecnico(),
					request.getCompanyTelefone(),
					request.getCompanyEndereco(),
					request.getCompanyEmpresaPrivada(),
					request.getProjetoJustificativa(),
					request.getProjetoResultadosEsperados(),
					request.getFases(),
					request.getCronograma(),
					request.getEquipe(),
					request.getPlanoAplicacao(),
					request.getCronogramaFinanceiro(),
					request.getContratanteNome(),
					request.getContratanteCargo(),
					request.getDataAssinatura()
			);

			// Gerar o documento
			byte[] document = planoDeTrabalho.gerarPlanoDeTrabalho(completeData);
			if (document == null || document.length == 0) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar o plano de trabalho: arquivo vazio.".getBytes());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",
					"attachment; filename=Plano_de_Trabalho_" + project.getProjectReference() +
					".docx");

			logger.info("Plano de trabalho gerado com sucesso para o projeto ID: {}", request.getProjectId());
			return ResponseEntity.ok().headers(headers).body(document);

		} catch (Exception e) {
			logger.error("Erro ao gerar o plano de trabalho", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Erro: " + e.getMessage()).getBytes());
		}
	}
}
