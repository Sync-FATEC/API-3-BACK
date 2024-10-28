package com.sync.api.dto.project.Dashboard;

public record ProjectStatusCount(
        Long naoIniciados,
        Long emAndamento,
        Long finalizados
) {
}
