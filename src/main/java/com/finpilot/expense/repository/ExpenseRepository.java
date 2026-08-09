package com.finpilot.expense.repository;

import com.finpilot.expense.entity.Expense;
import com.finpilot.expense.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByCategory(ExpenseCategory category);

    List<Expense> findByExpenseDate(LocalDate expenseDate);

    List<Expense> findByUserEmail(String email);

    List<Expense> findByUserEmailAndCategory(
            String email,
            ExpenseCategory category
    );
    List<Expense> findByUserEmailAndExpenseDate(
            String email,
            LocalDate expenseDate
    );
}