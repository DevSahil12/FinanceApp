package com.financeapp.ui.goals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.financeapp.data.model.Goal;
import com.financeapp.data.repository.GoalRepository;
import com.financeapp.data.repository.TransactionRepository;
import com.financeapp.utils.DateUtils;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private final GoalRepository goalRepo;
    private final TransactionRepository txRepo;

    public final LiveData<List<Goal>> allGoals;
    public final LiveData<List<Goal>> savingsGoals;
    public final LiveData<List<Goal>> budgetChallenges;

    private final MutableLiveData<String> snackMessage = new MutableLiveData<>();
    public LiveData<String> getSnackMessage() { return snackMessage; }

    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepo  = new GoalRepository(application);
        txRepo    = new TransactionRepository(application);
        allGoals  = goalRepo.getAllGoals();
        savingsGoals     = goalRepo.getSavingsGoals();
        budgetChallenges = goalRepo.getBudgetChallenges();
    }

    public void insert(Goal goal) { goalRepo.insert(goal); }
    public void update(Goal goal) { goalRepo.update(goal); }
    public void delete(Goal goal) { goalRepo.delete(goal); }

    public void addToSavings(int goalId, double amount) {
        com.financeapp.data.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            Goal goal = goalRepo.getGoalByIdSync(goalId);
            if (goal == null) return;
            double newAmount = Math.min(goal.savedAmount + amount, goal.targetAmount);
            goalRepo.updateSavedAmount(goalId, newAmount);
            if (newAmount >= goal.targetAmount) {
                snackMessage.postValue("🎉 Goal \"" + goal.title + "\" completed!");
            }
        });
    }

    // Refresh budget challenge progress from actual transactions
    public void refreshBudgetChallenges() {
        com.financeapp.data.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            String currentMonth = DateUtils.getCurrentMonthKey();
            List<Goal> challenges = goalRepo.getChallengesForMonth(currentMonth);
            long start = DateUtils.getMonthStart();
            long end   = DateUtils.getMonthEnd();

            for (Goal g : challenges) {
                double spent;
                if (g.budgetCategory == null || g.budgetCategory.equals("All Categories")) {
                    spent = txRepo.getTotalSpend(start, end);
                } else {
                    spent = txRepo.getCategorySpend(g.budgetCategory, start, end);
                }
                goalRepo.updateSavedAmount(g.id, spent);
            }
        });
    }
}
