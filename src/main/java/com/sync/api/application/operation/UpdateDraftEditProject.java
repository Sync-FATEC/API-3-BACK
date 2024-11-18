package com.sync.api.application.operation;

import com.sync.api.application.service.SensitiveFieldUtil;
import com.sync.api.domain.model.DraftEditProject;
import com.sync.api.infra.repository.DraftEditProjectRepository;
import com.sync.api.web.dto.project.UpdateProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateDraftEditProject {
    @Autowired
    private DraftEditProjectRepository draftEditProjectRepository;

    public DraftEditProject update(UpdateProjectDto updateProjectDto, DraftEditProject draft){
        if (updateProjectDto.projectReference() != null){
            draft.setDraftEditProjectReference(updateProjectDto.projectReference());
        }
        if(updateProjectDto.projectCompany() != null){
            draft.setDraftEditProjectCompany(updateProjectDto.projectCompany());
        }
        if (updateProjectDto.projectObjective() != null){
            draft.setDraftEditProjectObjective(updateProjectDto.projectObjective());
        }
        if(updateProjectDto.projectDescription() != null){
            draft.setDraftEditProjectDescription(updateProjectDto.projectDescription());
        }

        if(updateProjectDto.projectValue() != null){
            draft.setDraftEditProjectValue(updateProjectDto.projectValue());
        }
        if(updateProjectDto.projectStartDate() != null){
            draft.setDraftEditProjectStartDate(updateProjectDto.projectStartDate());
        }
        if(updateProjectDto.projectEndDate() != null){
            draft.setDraftEditProjectEndDate(updateProjectDto.projectEndDate());
        }
        if( updateProjectDto.projectClassification() != null){
            draft.setDraftEditProjectClassification(updateProjectDto.projectClassification());
        }
        draft.setSensitiveFields(SensitiveFieldUtil.getSensitiveFields(updateProjectDto));

        return draftEditProjectRepository.save(draft);
    }
}
