package com.finpilot.budget.dto;

import com.finpilot.enums.BudgetPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRequest {

    private String name;

    private BigDecimal amount;

    private BudgetPeriod period;

    private LocalDate startDate;

    private LocalDate endDate;

    public BudgetRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BudgetPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BudgetPeriod period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}