package com.sync.api.web.controller;

import com.sync.api.application.operation.exporter.PlanoDeTrabalho;
import com.sync.api.domain.model.Company;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.domain.model.WorkPlanCompleteData;
import com.sync.api.infra.repository.CompanyRepository;
import com.sync.api.infra.repository.CoordinatorsRepository;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.web.dto.documents.MinimalWorkPlanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/plano-de-trabalho")
public class PlanoDeTrabalhoController {

	private static final Logger logger = LoggerFactory.getLogger(PlanoDeTrabalhoController.class);

	@Autowired
	private PlanoDeTrabalho planoDeTrabalho;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private CoordinatorsRepository coordinatorRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@PostMapping("/gerar")
	public ResponseEntity<byte[]> generateWorkPlan(@RequestBody MinimalWorkPlanRequest request) {
		try {
			logger.info("Received request: projectId={}, coordinatorName={}, corporateName={}",
					request.getProjectId(), request.getNameCoordinator(), request.getProjectCompany());


			// Buscar os dados do projeto pelo ID
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + request.getProjectId()));

			// Buscar os dados do coordenador, se necessário
			Coordinators coordinator = coordinatorRepository.findByCoordinatorName(request.getNameCoordinator())
					.orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com o nome: " + request.getNameCoordinator()));

			Company empresa = companyRepository.findByCorporateName(request.getProjectCompany())
					.orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada com o nome: " + request.getProjectCompany()));

			// Montar o payload completo
			WorkPlanCompleteData completeData = new WorkPlanCompleteData(
					project.getProjectId(),
					project.getProjectReference(),
					project.getProjectTitle(),
					request.getProjectCompany(),
					empresa.getPhone(),
					empresa.getCpnj(),
					empresa.getPhone(),
					project.getProjectObjective(),
					coordinator.getCoordinatorName(),
					coordinator.getCoordinatorEconomicActivity(),
					coordinator.getCoordinatorCPF(),
					coordinator.getCoordinatorTelefone(),
					project.getProjectStartDate().toString()
			);

			// Gerar o documento
			byte[] document = planoDeTrabalho.gerarPlanoDeTrabalho(completeData);
			if (document == null || document.length == 0) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar o plano de trabalho: arquivo vazio.".getBytes());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",
					"attachment; filename=PlanoDeTrabalho_" + project.getProjectReference() +
					".docx");

			logger.info("Work plan generated successfully for project ID: {}", request.getProjectId());
			return ResponseEntity.ok().headers(headers).body(document);

		} catch (Exception e) {
			logger.error("Error generating work plan for project ID: {}", request.getProjectId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Erro: " + e.getMessage()).getBytes());
		}
	}
}
