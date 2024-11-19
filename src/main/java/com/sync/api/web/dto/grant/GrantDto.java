package com.sync.api.web.dto.grant;

public record GrantDto(
        String type,
        int months,
        int years,
        String acting,
        boolean active
) {}
