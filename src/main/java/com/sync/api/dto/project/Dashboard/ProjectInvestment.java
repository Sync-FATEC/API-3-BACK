package com.sync.api.dto.project.Dashboard;

public record ProjectInvestment(Long totalInvestment) {
    public ProjectInvestment(Long totalInvestment) {
        this.totalInvestment = totalInvestment != null ? totalInvestment : 0L;
    }
}