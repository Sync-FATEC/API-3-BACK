package com.sync.api.web.dto.workplan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CronogramaDTO {
	private String atividade;
	private String indicadorFisico;
	private String dataInicio;
	private String dataFim;
}

