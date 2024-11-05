package com.sync.api.application.specification;

import com.sync.api.domain.enums.ProjectClassification;
import com.sync.api.domain.enums.ProjectStatus;
import com.sync.api.domain.model.Project;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class ProjectSpecification {

    public static Specification<Project> filterByCriteria(String keyword, LocalDate projectStartDate, LocalDate projectEndDate, ProjectStatus status, ProjectClassification classification) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (keyword != null && !keyword.isEmpty()) {
                String[] wordsList = keyword.split(" ");
                for (String word : wordsList) {
                    predicate = builder.and(predicate, builder.or(
                            builder.like(root.get("projectObjective"), "%" + word + "%"),
                            builder.like(root.get("projectDescription"), "%" + word + "%"),
                            builder.like(root.get("projectCompany"), "%" + word + "%"),
                            builder.like(root.get("projectReference"), "%" + word + "%"),
                            builder.like(root.get("nameCoordinator"), "%" + word + "%")
                    ));
                }
            }

            if (projectStartDate != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("projectStartDate"), projectStartDate));
            }

            if (projectEndDate != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("projectEndDate"), projectEndDate));
            }

            if (status != null) {
                predicate = builder.and(predicate, builder.equal(root.get("projectStatus"), status));
            }

            if (classification != null) {
                predicate = builder.and(predicate, builder.equal(root.get("projectClassification"), classification));
            }

            return predicate;
        };
    }
}