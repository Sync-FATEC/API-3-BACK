package com.sync.api.web.dto.workplan;

import com.sync.api.domain.model.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CronogramaDTO {
	private String atividade;
	private String indicadorFisico;
	private String dataInicio;
	private String dataFim;

	public static Schedule toEntity(CronogramaDTO dto) {
		Schedule schedule = new Schedule();
		schedule.setActivity(dto.getAtividade());
		schedule.setPhysicalIndicator(dto.getIndicadorFisico());
		schedule.setStartDate(dto.getDataInicio());
		schedule.setEndDate(dto.getDataFim());
		return schedule;
	}
}

