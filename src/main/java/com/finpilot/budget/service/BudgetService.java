package com.finpilot.budget.service;

import com.finpilot.budget.dto.BudgetRequest;
import com.finpilot.budget.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {

    BudgetResponse createBudget(BudgetRequest request);

    BudgetResponse getBudgetById(Long id);

    List<BudgetResponse> getAllBudgets();

    BudgetResponse updateBudget(Long id, BudgetRequest request);

    void deleteBudget(Long id);
}