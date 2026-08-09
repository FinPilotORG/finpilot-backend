package com.finpilot.budget.service.impl;

import com.finpilot.budget.dto.BudgetRequest;
import com.finpilot.budget.dto.BudgetResponse;
import com.finpilot.budget.exception.BudgetNotFoundException;
import com.finpilot.budget.mapper.BudgetMapper;
import com.finpilot.budget.repository.BudgetRepository;
import com.finpilot.budget.service.BudgetService;
import com.finpilot.entity.Budget;
import com.finpilot.entity.User;
import com.finpilot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetServiceImpl(
            BudgetRepository budgetRepository,
            UserRepository userRepository) {

        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));
    }


    @Override
    public BudgetResponse createBudget(BudgetRequest request) {

        User currentUser = getCurrentUser();

        Budget budget = BudgetMapper.toEntity(request);

        budget.setUser(currentUser);

        Budget savedBudget = budgetRepository.save(budget);

        return BudgetMapper.toResponse(savedBudget);
    }

    @Override
    public BudgetResponse getBudgetById(Long id) {

        Budget budget;

        if (isAdmin()) {

            budget = budgetRepository.findById(id)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));

        } else {

            User currentUser = getCurrentUser();

            budget = budgetRepository
                    .findByIdAndUser(id, currentUser)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));
        }

        return BudgetMapper.toResponse(budget);
    }

    @Override
    public List<BudgetResponse> getAllBudgets() {

        List<Budget> budgets;

        if (isAdmin()) {

            budgets = budgetRepository.findAll();

        } else {

            User currentUser = getCurrentUser();

            budgets = budgetRepository.findByUser(currentUser);
        }

        return budgets.stream()
                .map(BudgetMapper::toResponse)
                .toList();
    }

    @Override
    public BudgetResponse updateBudget(
            Long id,
            BudgetRequest request) {

        Budget budget;

        if (isAdmin()) {

            budget = budgetRepository.findById(id)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));

        } else {

            User currentUser = getCurrentUser();

            budget = budgetRepository
                    .findByIdAndUser(id, currentUser)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));
        }

        budget.setName(request.getName());
        budget.setAmount(request.getAmount());
        budget.setPeriod(request.getPeriod());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        Budget updatedBudget = budgetRepository.save(budget);

        return BudgetMapper.toResponse(updatedBudget);
    }

    @Override
    public void deleteBudget(Long id) {

        Budget budget;

        if (isAdmin()) {

            budget = budgetRepository.findById(id)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));

        } else {

            User currentUser = getCurrentUser();

            budget = budgetRepository
                    .findByIdAndUser(id, currentUser)
                    .orElseThrow(() ->
                            new BudgetNotFoundException(id));
        }

        budgetRepository.delete(budget);
    }
    private boolean isAdmin() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }
}