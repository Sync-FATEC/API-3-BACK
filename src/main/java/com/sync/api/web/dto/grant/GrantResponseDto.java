package com.sync.api.web.dto.grant;

public record GrantResponseDto(
        String id,
        String type,
        String duration,
        String acting,
        boolean active
) {
}
