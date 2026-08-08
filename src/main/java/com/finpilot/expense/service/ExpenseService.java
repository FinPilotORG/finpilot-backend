package com.finpilot.expense.service;

import com.finpilot.expense.dto.ExpenseRequest;
import com.finpilot.expense.dto.ExpenseResponse;
import com.finpilot.expense.enums.ExpenseCategory;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request);

    ExpenseResponse getExpenseById(Long id);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    void deleteExpense(Long id);

    List<ExpenseResponse> getExpensesByCategory(ExpenseCategory category);

    List<ExpenseResponse> getExpensesByDate(LocalDate expenseDate);
}