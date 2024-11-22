package com.sync.api.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkPlanCompleteData {
	private String projectId;
	private String projectReference;
	private String projectTitle;
	private String companyName;
	private String companyPhone;
	private String cnpj;
	private String phone;
	private String projectObjective;
	private String coordinatorName;
	private String coordinatorEconomicActivity;
	private String coordinatorCpf;
	private String coordinatorTelefone;
	private String projectStartDate;

	public WorkPlanCompleteData(String projectId, String projectReference, String projectTitle, String companyName,
	                            String companyPhone,
	                            String cnpj,
	                            String phone,
	                            String projectObjective,
	                            String coordinatorName, String coordinatorEconomicActivity, String coordinatorCpf,
	                            String coordinatorTelefone,
	                            String projectStartDate) {
		this.projectId = projectId;
		this.projectReference = projectReference;
		this.projectTitle = projectTitle;
		this.companyName = companyName;
		this.companyPhone = companyPhone;
		this.cnpj = cnpj;
		this.phone = phone;
		this.projectObjective = projectObjective;
		this.coordinatorName = coordinatorName;
		this.coordinatorCpf = coordinatorCpf;
		this.coordinatorEconomicActivity = coordinatorEconomicActivity;
		this.coordinatorTelefone = coordinatorTelefone;
		this.projectStartDate = projectStartDate;
	}
}
