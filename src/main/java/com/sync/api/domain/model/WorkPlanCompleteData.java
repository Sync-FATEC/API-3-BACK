package com.sync.api.domain.model;

import com.sync.api.web.dto.workplan.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class WorkPlanCompleteData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Dados do Projeto
	private String projectId;
	private String projectReference;
	private String projectTitle;
	private LocalDate projectStartDate;
	@Lob
	@Column(columnDefinition = "TEXT")
	private String projectObjective;

	// Dados do Coordenador
	private String coordinatorName;
	private String coordinatorCPF;
	private String coordinatorAddress;
	private String coordinatorCity;
	private String coordinatorUF;
	private String coordinatorCEP;
	private String coordinatorTelefone;
	private String coordinatorEconomicActivity;
	private String coordinatorPeriod;

	// Dados da Empresa
	private String companyRazaoSocial;
	private String companyCNPJ;
	private String companyResponsavelTecnico;
	private String companyTelefone;
	private String companyEndereco;
	private String companyEmpresaPrivada;

	// Dados do Projeto
	private String projetoJustificativa;
	private String projetoResultadosEsperados;

	// Listas
	@OneToMany(mappedBy = "workPlan")
	private List<Phases> phases;

	@OneToMany(mappedBy = "workPlan")
	private List<Schedule> schedules;

	@OneToMany(mappedBy = "workPlan")
	private List<Team> teams;

	@OneToMany(mappedBy = "workPlan")
	private List<Plan> applicationPlans;

	private List<String> cronogramaFinanceiro;

	// Contratante
	private String contratanteNome;
	private String contratanteCargo;
	private String dataAssinatura;

	@OneToOne
	private Project project;

	// Construtor completo
	public WorkPlanCompleteData(String projectId,
	                            String projectReference,
	                            String projectTitle,
	                            LocalDate projectStartDate,
	                            String projectObjective,
								String coordinatorName,
	                            String coordinatorCPF,
	                            String coordinatorAddress,
	                            String coordinatorCity,
	                            String coordinatorUF,
	                            String coordinatorCEP,
	                            String coordinatorTelefone,
	                            String coordinatorEconomicActivity,
	                            String coordinatorPeriod,
	                            String companyRazaoSocial,
	                            String companyCNPJ,
	                            String companyResponsavelTecnico,
	                            String companyTelefone,
	                            String companyEndereco,
	                            String companyEmpresaPrivada,
	                            String projetoJustificativa,
	                            String projetoResultadosEsperados,
	                            List<FaseDTO> fases,
	                            List<CronogramaDTO> cronograma,
	                            List<EquipeDTO> equipe,
	                            List<PlanoAplicacaoDTO> planoAplicacao,
	                            List<CronogramaFinanceiroDTO> cronogramaFinanceiro,
	                            String contratanteNome,
	                            String contratanteCargo,
	                            String dataAssinatura) {
		this.projectId = projectId;
		this.projectReference = projectReference;
		this.projectTitle = projectTitle;
		this.projectStartDate = projectStartDate;
		this.projectObjective = projectObjective;
		this.coordinatorName = coordinatorName;
		this.coordinatorCPF = coordinatorCPF;
		this.coordinatorAddress = coordinatorAddress;
		this.coordinatorCity = coordinatorCity;
		this.coordinatorUF = coordinatorUF;
		this.coordinatorCEP = coordinatorCEP;
		this.coordinatorTelefone = coordinatorTelefone;
		this.coordinatorEconomicActivity = coordinatorEconomicActivity;
		this.coordinatorPeriod = coordinatorPeriod;
		this.companyRazaoSocial = companyRazaoSocial;
		this.companyCNPJ = companyCNPJ;
		this.companyResponsavelTecnico = companyResponsavelTecnico;
		this.companyTelefone = companyTelefone;
		this.companyEndereco = companyEndereco;
		this.companyEmpresaPrivada = companyEmpresaPrivada;
		this.projetoJustificativa = projetoJustificativa;
		this.projetoResultadosEsperados = projetoResultadosEsperados;
		this.phases = fases.stream().map(FaseDTO::toEntity).collect(Collectors.toList());
		this.schedules = cronograma.stream().map(CronogramaDTO::toEntity).collect(Collectors.toList());
		this.teams = equipe.stream().map(EquipeDTO::toEntity).collect(Collectors.toList());
		this.applicationPlans = planoAplicacao.stream().map(PlanoAplicacaoDTO::toEntity).collect(Collectors.toList());
		this.cronogramaFinanceiro = cronogramaFinanceiro.stream().map(CronogramaFinanceiroDTO::getValor).collect(Collectors.toList());
		this.contratanteNome = contratanteNome;
		this.contratanteCargo = contratanteCargo;
		this.dataAssinatura = dataAssinatura;
	}
}
