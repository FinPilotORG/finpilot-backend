package com.finpilot.expense.mapper;

import com.finpilot.expense.dto.ExpenseRequest;
import com.finpilot.expense.dto.ExpenseResponse;
import com.finpilot.expense.entity.Expense;

public class ExpenseMapper {

    public static Expense toEntity(ExpenseRequest request) {

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());

        return response;
    }
}