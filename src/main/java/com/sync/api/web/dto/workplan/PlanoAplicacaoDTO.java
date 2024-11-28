package com.sync.api.web.dto.workplan;

import com.sync.api.domain.model.Plan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoAplicacaoDTO {
	private String descricao;
	private String total;

	public static Plan toEntity(PlanoAplicacaoDTO dto) {
		Plan plan = new Plan();
		plan.setDescription(dto.getDescricao());
		plan.setTotal(dto.getTotal());
		return plan;
	}
}
