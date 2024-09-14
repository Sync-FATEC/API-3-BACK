package com.sync.api.operation.contract;
import com.sync.api.model.Project;

public interface Exporter {
    byte[] export (Project project);
}
