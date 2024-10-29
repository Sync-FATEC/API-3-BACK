package com.sync.api.dto.project.Dashboard;

public record ProjectClassificationCount(
        Long outros,
        Long contratos,
        Long convenio,
        Long patrocinio,
        Long termoDeCooperacao,
        Long termoDeOutorga
) {
}
