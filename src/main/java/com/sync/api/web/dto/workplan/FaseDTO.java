package com.sync.api.web.dto.workplan;

import com.sync.api.domain.model.Phases;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaseDTO {
	private String fase;
	private String descricao;

	public static Phases toEntity(FaseDTO dto) {
		Phases phase = new Phases();
		phase.setPhase(dto.getFase());
		phase.setDescription(dto.getDescricao());
		return phase;
	}
}

