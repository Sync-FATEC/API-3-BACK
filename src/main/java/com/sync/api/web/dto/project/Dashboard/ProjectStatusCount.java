package com.sync.api.web.dto.project.Dashboard;

public record ProjectStatusCount(
        Long naoIniciados,
        Long emAndamento,
        Long finalizados
) {
}
