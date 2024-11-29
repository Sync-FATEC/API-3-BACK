package com.sync.api.web.dto.grant;

import lombok.Data;

@Data
public class UpdateGrantDto {
    String id;
    String type;
    int months;
    int years;
    String acting;
    boolean active;
}
