package com.sync.api.web.dto.workplan;

import com.sync.api.domain.model.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipeDTO {
	private String nome;
	private String instituicao;
	private String competencia;

	public static Team toEntity(EquipeDTO dto) {
		Team team = new Team();
		team.setName(dto.getNome());
		team.setInstitution(dto.getInstituicao());
		team.setCompetence(dto.getCompetencia());
		return team;
	}
}
