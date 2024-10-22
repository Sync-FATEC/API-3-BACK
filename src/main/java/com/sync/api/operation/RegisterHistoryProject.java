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
        newHistory.setChangedFields(historyProjectDto.getChangedFields()); // Usando o getter
        newHistory.setNewValues(historyProjectDto.getNewValues());         // Usando o getter
        newHistory.setOldValues(historyProjectDto.getOldValues());         // Usando o getter
        newHistory.setChangeDate(LocalDate.now());

        if(historyProjectDto.getProject() != null){                        // Usando o getter
            newHistory.setProject(historyProjectDto.getProject());
        }
        if (historyProjectDto.getDocuments() != null){                     // Usando o getter
            newHistory.setDocuments(historyProjectDto.getDocuments());
        }
        if (historyProjectDto.getUser() != null){                          // Usando o getter
            newHistory.setUser(historyProjectDto.getUser());
        }

        historyProjectRepository.save(newHistory);
    }
}
