package com.finpilot.expense.service.impl;

import com.finpilot.expense.dto.ExpenseRequest;
import com.finpilot.expense.dto.ExpenseResponse;
import com.finpilot.expense.entity.Expense;
import com.finpilot.expense.exception.ExpenseNotFoundException;
import com.finpilot.expense.mapper.ExpenseMapper;
import com.finpilot.expense.repository.ExpenseRepository;
import com.finpilot.expense.enums.ExpenseCategory;
import com.finpilot.expense.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseRepository expenseRepository;
    public ExpenseServiceImpl(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }
    @Override
    public ExpenseResponse createExpense(ExpenseRequest request){
        Expense expense= ExpenseMapper.toEntity(request);
        Expense savedExpense=expenseRepository.save(expense);
        return ExpenseMapper.toResponse(savedExpense);

    }
    @Override
    public ExpenseResponse getExpenseById(Long id){
        Expense expense=expenseRepository.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found with id : " + id));
        return ExpenseMapper.toResponse(expense);
    }
    @Override
    public List<ExpenseResponse> getAllExpenses(){
        return expenseRepository.findAll().stream().map(ExpenseMapper::toResponse).toList();

    }
    @Override
    public ExpenseResponse updateExpense(Long id,ExpenseRequest request){
        Expense expense=expenseRepository.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found with id : " + id));
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());
        Expense updatedExpense=expenseRepository.save(expense);
        return ExpenseMapper.toResponse(updatedExpense);

    }
    @Override
    public void deleteExpense(Long id){
        Expense expense=expenseRepository.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found with id : " + id));
        expenseRepository.delete(expense);

    }
    @Override
    public List<ExpenseResponse> getExpensesByCategory(ExpenseCategory category){
        return expenseRepository.findByCategory(category)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();

    }
    @Override
    public List<ExpenseResponse> getExpensesByDate(LocalDate expenseDate){
        return expenseRepository.findByExpenseDate(expenseDate)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();

    }


}
