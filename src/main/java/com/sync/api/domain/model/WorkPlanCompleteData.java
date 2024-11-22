package com.sync.api.domain.model;

import com.sync.api.web.dto.workplan.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WorkPlanCompleteData {

	// Dados do Projeto
	private String projectId;
	private String projectReference;
	private String projectTitle;
	private LocalDate projectStartDate;
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
	private List<FaseDTO> fases;
	private List<CronogramaDTO> cronograma;
	private List<EquipeDTO> equipe;
	private List<PlanoAplicacaoDTO> planoAplicacao;
	private List<CronogramaFinanceiroDTO> cronogramaFinanceiro;

	// Contratante
	private String contratanteNome;
	private String contratanteCargo;
	private String dataAssinatura;

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
		this.fases = fases;
		this.cronograma = cronograma;
		this.equipe = equipe;
		this.planoAplicacao = planoAplicacao;
		this.cronogramaFinanceiro = cronogramaFinanceiro;
		this.contratanteNome = contratanteNome;
		this.contratanteCargo = contratanteCargo;
		this.dataAssinatura = dataAssinatura;
	}
}
