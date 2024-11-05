package com.sync.api.application.operation;

import com.sync.api.web.dto.project.UserDto;
import com.sync.api.domain.model.User;

public class UserMapper {
    public static UserDto toDTO(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUserEmail(user.getUserEmail());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserEmail(dto.getUserEmail());
        return user;
    }
}
