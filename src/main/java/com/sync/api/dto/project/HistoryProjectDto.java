package com.sync.api.dto.project;

import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryProjectDto {
        @Lob
        @Column(columnDefinition = "TEXT")
        private String changedFields;

        @Lob
        @Column(columnDefinition = "TEXT")
        private String newValues;

        @Lob
        @Column(columnDefinition = "TEXT")
        private String oldValues;

        private Project project;
        private Documents documents;
        private User user;

}