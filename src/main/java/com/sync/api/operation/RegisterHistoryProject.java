package com.sync.api.operation;

import com.sync.api.dto.project.HistoryProjectDto;
import com.sync.api.model.HistoryProject;
import com.sync.api.repository.HistoryProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegisterHistoryProject {
    @Autowired
    private HistoryProjectRepository historyProjectRepository;

    public void registerLog(HistoryProjectDto historyProjectDto){
        HistoryProject newHistory = new HistoryProject();
        newHistory.setChangedFields(historyProjectDto.getChangedFields());
        newHistory.setNewValues(historyProjectDto.getNewValues());
        newHistory.setOldValues(historyProjectDto.getOldValues());
        newHistory.setChangeDate(LocalDate.now());
        if(historyProjectDto.getProject() != null){
            newHistory.setProject(historyProjectDto.getProject());
        }
        if (historyProjectDto.getDocuments() != null){
            newHistory.setDocuments(historyProjectDto.getDocuments());
        }
        if (historyProjectDto.getUser() != null){
            newHistory.setUser(historyProjectDto.getUser());
        }
        historyProjectRepository.save(newHistory);

    }
}
