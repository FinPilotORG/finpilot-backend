package com.finpilot.budget.mapper;

import com.finpilot.budget.dto.BudgetRequest;
import com.finpilot.budget.dto.BudgetResponse;
import com.finpilot.entity.Budget;

public class BudgetMapper {

    public static Budget toEntity(BudgetRequest request) {

        Budget budget = new Budget();

        budget.setName(request.getName());
        budget.setAmount(request.getAmount());
        budget.setPeriod(request.getPeriod());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        return budget;
    }

    public static BudgetResponse toResponse(Budget budget) {

        BudgetResponse response = new BudgetResponse();

        response.setId(budget.getId());

        if (budget.getUser() != null) {
            response.setUserId(budget.getUser().getId());
        }

        response.setName(budget.getName());
        response.setAmount(budget.getAmount());
        response.setPeriod(budget.getPeriod());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());

        return response;
    }
}