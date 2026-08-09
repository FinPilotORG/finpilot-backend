package com.finpilot.expense.service.impl;
import com.finpilot.entity.User;
import com.finpilot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.finpilot.expense.dto.ExpenseRequest;
import com.finpilot.expense.dto.ExpenseResponse;
import com.finpilot.expense.entity.Expense;
import com.finpilot.expense.exception.ExpenseNotFoundException;
import com.finpilot.expense.mapper.ExpenseMapper;
import com.finpilot.expense.repository.ExpenseRepository;
import com.finpilot.expense.enums.ExpenseCategory;
import com.finpilot.expense.service.ExpenseService;
import com.finpilot.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }
    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Logged-in user not found")
                );

        Expense expense = ExpenseMapper.toEntity(request);

        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }
    @Override
    public ExpenseResponse getExpenseById(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(
                                "Expense not found with id : " + id));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !expense.getUser().getEmail().equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to access this expense"
            );
        }

        return ExpenseMapper.toResponse(expense);
    }
    @Override
    public List<ExpenseResponse> getAllExpenses() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        List<Expense> expenses;

        if (isAdmin) {
            expenses = expenseRepository.findAll();
        } else {
            expenses = expenseRepository.findByUserEmail(email);
        }

        return expenses.stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }
    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(
                                "Expense not found with id : " + id));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !expense.getUser().getEmail().equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to update this expense"
            );
        }

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(updatedExpense);
    }
    @Override
    public void deleteExpense(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(
                                "Expense not found with id : " + id));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !expense.getUser().getEmail().equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to delete this expense"
            );
        }

        expenseRepository.delete(expense);
    }
    @Override
    public List<ExpenseResponse> getExpensesByCategory(
            ExpenseCategory category) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        List<Expense> expenses;

        if (isAdmin) {
            expenses = expenseRepository.findByCategory(category);
        } else {
            expenses = expenseRepository.findByUserEmailAndCategory(
                    email,
                    category
            );
        }

        return expenses.stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }
    @Override
    public List<ExpenseResponse> getExpensesByDate(
            LocalDate expenseDate) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        List<Expense> expenses;

        if (isAdmin) {
            expenses = expenseRepository.findByExpenseDate(expenseDate);
        } else {
            expenses = expenseRepository.findByUserEmailAndExpenseDate(
                    email,
                    expenseDate
            );
        }

        return expenses.stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }


}
