package com.sync.api.service;

import com.sync.api.dto.project.RegisterProjectDTO;

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
}
