package com.sync.api.operation;

import com.sync.api.dto.HistoryProjectDto;
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
        newHistory.setChangedFields(historyProjectDto.changedFields());
        newHistory.setNewValues(historyProjectDto.newValues());
        newHistory.setOldValues(historyProjectDto.oldValues());
        newHistory.setChangeDate(LocalDate.now());
        if(historyProjectDto.project() != null){
            newHistory.setProject(historyProjectDto.project());
        }
        if (historyProjectDto.documents() != null){
            newHistory.setDocuments(historyProjectDto.documents());
        }
        if (historyProjectDto.user() != null){
            newHistory.setUser(historyProjectDto.user());
        }
        historyProjectRepository.save(newHistory);

    }
}
