package com.sync.api.web.dto.project.Dashboard;

public record ProjectClassificationCount(
        Long outros,
        Long contratos,
        Long convenio,
        Long patrocinio,
        Long termoDeCooperacao,
        Long termoDeOutorga
) {
}
