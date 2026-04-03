package com.financeapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.financeapp.data.db.AppDatabase;
import com.financeapp.data.db.GoalDao;
import com.financeapp.data.model.Goal;

import java.util.List;

public class GoalRepository {

    private final GoalDao dao;

    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.goalDao();
    }

    public LiveData<List<Goal>> getAllGoals() { return dao.getAllGoals(); }
    public LiveData<List<Goal>> getSavingsGoals() { return dao.getSavingsGoals(); }
    public LiveData<List<Goal>> getBudgetChallenges() { return dao.getBudgetChallenges(); }

    public void insert(Goal goal) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.insert(goal));
    }

    public void update(Goal goal) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.update(goal));
    }

    public void delete(Goal goal) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.delete(goal));
    }

    public void updateSavedAmount(int id, double amount) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.updateSavedAmount(id, amount));
    }

    public void updateStreak(int id, int streak, int longest) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.updateStreak(id, streak, longest));
    }

    public Goal getGoalByIdSync(int id) {
        return dao.getGoalByIdSync(id);
    }

    public List<Goal> getChallengesForMonth(String month) {
        return dao.getChallengesForMonth(month);
    }
}
