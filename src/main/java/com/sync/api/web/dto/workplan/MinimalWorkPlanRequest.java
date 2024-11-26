package com.sync.api.web.dto.workplan;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MinimalWorkPlanRequest {
	private String projectId;
	private String coordinatorCPF;
	private String coordinatorAddress;
	private String coordinatorCity;
	private String coordinatorUF;
	private String coordinatorCEP;
	private String coordinatorTelefone;
	private String coordinatorEconomicActivity;
	private String coordinatorPeriod;
	private String companyRazaoSocial;
	private String companyCNPJ;
	private String companyResponsavelTecnico;
	private String companyTelefone;
	private String companyEndereco;
	private String companyEmpresaPrivada;
	private String projetoJustificativa;
	private String projetoResultadosEsperados;
	private List<FaseDTO> fases;
	private List<CronogramaDTO> cronograma;
	private List<EquipeDTO> equipe;
	private List<PlanoAplicacaoDTO> planoAplicacao;
	private List<CronogramaFinanceiroDTO> cronogramaFinanceiro;
	private String contratanteNome;
	private String contratanteCargo;
	private String dataAssinatura;
}
