package com.sync.api.application.operation.contract;
import com.sync.api.domain.model.Project;

public interface Exporter {
    byte[] export (Project project);
}
