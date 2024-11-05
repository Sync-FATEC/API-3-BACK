package com.sync.api.web.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sync.api.domain.model.Documents;
import com.sync.api.domain.model.Project;
import com.sync.api.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
        private LocalDateTime changeDate;

        @JsonIgnore
        private Project project;
        private Documents documents;
        @JsonIgnore
        private User User;
        private String userEmail;

}