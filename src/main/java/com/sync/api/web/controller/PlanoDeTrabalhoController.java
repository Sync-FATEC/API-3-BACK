package com.sync.api.web.controller;

import com.sync.api.application.operation.exporter.PlanoDeTrabalho;
import com.sync.api.domain.model.Coordinators;
import com.sync.api.domain.model.PlanWordFAPG;
import com.sync.api.domain.model.WorkPlanCompleteData;
import com.sync.api.infra.repository.PlanWordFAPGRepository;
import com.sync.api.infra.repository.ProjectRepository;
import com.sync.api.infra.repository.WorkPlanCompleteDataRepository;
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

	@Autowired
	private WorkPlanCompleteDataRepository workPlanCompleteDataRepository;

	@Autowired
	private PlanWordFAPGRepository planWordFAPGRepository;

	@PostMapping("/gerar")
	public ResponseEntity<byte[]> generateWorkPlan(@RequestBody MinimalWorkPlanRequest request) {
		try {
			logger.info("Recebendo requisição para gerar plano de trabalho...");
			logger.info("Dados recebidos: {}", request);

			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + request.getProjectId()));

			if (planWordFAPGRepository.findByProject_ProjectId(request.getProjectId()).isPresent()) {
				logger.info("Plano de trabalho já gerado para o projeto ID: {}", request.getProjectId());
				return getPlanWord(request.getProjectId());
			}

			Coordinators coordinator = project.getCoordinators();
			if (coordinator == null) {
				throw new IllegalArgumentException("Coordenador não encontrado para o projeto com ID: " + request.getProjectId());
			}

// Verificar os dados do coordenador e fornecer uma mensagem padrão caso não existam
			String coordinatorName = (coordinator.getCoordinatorName() != null) ? coordinator.getCoordinatorName() : "Coordenador não informado";
			String coordinatorCPF = (coordinator.getCoordinatorCPF() != null) ? coordinator.getCoordinatorCPF() : "CPF não informado";
			String coordinatorTelefone = (coordinator.getCoordinatorTelefone() != null) ? coordinator.getCoordinatorTelefone() : "Telefone não informado";
			String coordinatorEconomicActivity = (coordinator.getCoordinatorEconomicActivity() != null) ? coordinator.getCoordinatorEconomicActivity() : "Atividade econômica não informada";

// Verificar o endereço do coordenador, fornecendo uma mensagem padrão caso não existam
			String street = (coordinator.getAddress() != null && coordinator.getAddress().getStreet() != null) ? coordinator.getAddress().getStreet() : "Rua não informada";
			String number = (coordinator.getAddress() != null && coordinator.getAddress().getNumber() != null) ? coordinator.getAddress().getNumber() : "Número não informado";
			String neighborhood = (coordinator.getAddress() != null && coordinator.getAddress().getNeighborhood() != null) ? coordinator.getAddress().getNeighborhood() : "Bairro não informado";
			String city = (coordinator.getAddress() != null && coordinator.getAddress().getCity() != null) ? coordinator.getAddress().getCity() : "Cidade não informada";
			String state = (coordinator.getAddress() != null && coordinator.getAddress().getState() != null) ? coordinator.getAddress().getState() : "Estado não informado";
			String zipCode = (coordinator.getAddress() != null && coordinator.getAddress().getZipCode() != null) ? coordinator.getAddress().getZipCode() : "CEP não informado";

// Montar o endereço completo do coordenador
			String coordinatorAddress = street + ", " + number + " - " + neighborhood + ", " + city + " - " + state + " - " + zipCode;

// Verificar os dados da empresa e fornecer uma mensagem padrão caso não existam
			String companyRazaoSocial = (project.getCompany().getCorporateName() != null) ? project.getCompany().getCorporateName() : "Razão Social não informada";
			String companyCNPJ = (project.getCompany().getCnpj() != null) ? project.getCompany().getCnpj() : "CNPJ não informado";
			String companyResponsavelTecnico = (request.getCompanyResponsavelTecnico() != null) ? request.getCompanyResponsavelTecnico() : "Responsável técnico não informado";
			String companyTelefone = (project.getCompany().getPhone() != null) ? project.getCompany().getPhone() : "Telefone não informado";

// Verificar o endereço da empresa e fornecer uma mensagem padrão caso não existam
			String companyStreet = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getStreet() != null) ? project.getCompany().getAddress().getStreet() : "Rua não informada";
			String companyNumber = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getNumber() != null) ? project.getCompany().getAddress().getNumber() : "Número não informado";
			String companyNeighborhood = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getNeighborhood() != null) ? project.getCompany().getAddress().getNeighborhood() : "Bairro não informado";
			String companyCity = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getCity() != null) ? project.getCompany().getAddress().getCity() : "Cidade não informada";
			String companyState = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getState() != null) ? project.getCompany().getAddress().getState() : "Estado não informado";
			String companyZipCode = (project.getCompany().getAddress() != null && project.getCompany().getAddress().getZipCode() != null) ? project.getCompany().getAddress().getZipCode() : "CEP não informado";

// Montar o endereço completo da empresa
			String companyEndereco = companyStreet + ", " + companyNumber + " - " + companyNeighborhood + ", " + companyCity + " - " + companyState + " - " + companyZipCode;

// Transformar o valor de isPrivateCompany em uma string
			String companyType = (project.getCompany().isPrivateCompany()) ? "Empresa Privada" : "Empresa Pública";

// Agora, crie o objeto WorkPlanCompleteData com esses valores
			WorkPlanCompleteData completeData = new WorkPlanCompleteData(
					request.getProjectId(),
					project.getProjectReference(),
					project.getProjectTitle(),
					project.getProjectStartDate(),
					project.getProjectObjective(),
					coordinatorName,
					coordinatorCPF,
					coordinatorAddress,
					city, // Coordenador cidade
					state, // Coordenador estado
					zipCode, // Coordenador CEP
					coordinatorTelefone,
					coordinatorEconomicActivity,
					request.getCoordinatorPeriod(),
					companyRazaoSocial,
					companyCNPJ,
					companyResponsavelTecnico,
					companyTelefone,
					companyEndereco, // Endereço da empresa
					companyType,
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

			// Salvar os dados completos
			workPlanCompleteDataRepository.save(completeData);

			// Salvar o documento
			planWordFAPGRepository.save(new PlanWordFAPG(document, project.getProjectReference(), project));

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

	@GetMapping("/download/{projectid}")
	public ResponseEntity<byte[]> getPlanWord(@PathVariable String projectid) {
		try {
			if (projectid == null || projectid.isEmpty()) {
				return ResponseEntity.badRequest().body("ID do projeto não informado.".getBytes());
			}

			PlanWordFAPG planWord = planWordFAPGRepository.findByProject_ProjectId(projectid)
					.orElseThrow(() -> new IllegalArgumentException("Plano de trabalho não encontrado para o projeto com ID: " + projectid));

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition",
					"attachment; filename=Plano_de_Trabalho_" + planWord.getProjectReference() + ".docx");

			byte[] planWordFile = planWord.getPlanWordFile();

			if (planWordFile == null || planWordFile.length == 0) {
				// Se o arquivo estiver vazio ou nulo
				throw new IllegalStateException("Arquivo do plano de trabalho não encontrado ou está vazio.");
			}

			System.out.println("Tamanho do arquivo: " + planWordFile.length);

			return ResponseEntity.ok().headers(headers).body(planWordFile);
		} catch (Exception e) {
			logger.error("Erro ao baixar o plano de trabalho", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Erro: " + e.getMessage()).getBytes());
		}
	}
}
