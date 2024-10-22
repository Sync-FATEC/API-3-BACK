package com.sync.api.dto.project;

import com.sync.api.model.Documents;
import com.sync.api.model.Project;
import com.sync.api.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public record HistoryProjectDto(
        @Lob
        @Column(columnDefinition = "TEXT")
        String changedFields,
        @Lob
        @Column(columnDefinition = "TEXT")
        String newValues,
        @Lob
        @Column(columnDefinition = "TEXT")
        String oldValues,
        Project project,
        Documents documents,
        User user
) {
}

