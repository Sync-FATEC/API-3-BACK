package com.sync.api.application.service;

import com.sync.api.web.dto.project.RegisterProjectDTO;
import com.sync.api.web.dto.project.UpdateProjectDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SensitiveFieldUtil {

    public static List<String> getSensitiveFields(RegisterProjectDTO dto) {
        List<String> sensitiveFields = new ArrayList<>();

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType() == boolean.class && field.getBoolean(dto)) {
                    String fieldName = field.getName().replace("Sensitive", "");
                    sensitiveFields.add(fieldName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return sensitiveFields;
    }

    public static List<String> getSensitiveFields(UpdateProjectDto dto) {
        List<String> sensitiveFields = new ArrayList<>();

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType() == boolean.class && field.getBoolean(dto)) {
                    String fieldName = field.getName().replace("Sensitive", "");
                    sensitiveFields.add(fieldName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return sensitiveFields;
    }
}
