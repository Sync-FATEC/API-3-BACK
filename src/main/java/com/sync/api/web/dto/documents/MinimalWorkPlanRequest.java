package com.sync.api.web.dto.documents;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MinimalWorkPlanRequest {
	private String projectId;
	private String nameCoordinator;
	private String projectCompany;
}
