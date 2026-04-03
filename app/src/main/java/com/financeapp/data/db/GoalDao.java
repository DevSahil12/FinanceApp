package com.financeapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.financeapp.data.model.Goal;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM goals ORDER BY created_at DESC")
    LiveData<List<Goal>> getAllGoals();

    @Query("SELECT * FROM goals WHERE is_budget_challenge = 0 ORDER BY created_at DESC")
    LiveData<List<Goal>> getSavingsGoals();

    @Query("SELECT * FROM goals WHERE is_budget_challenge = 1 ORDER BY created_at DESC")
    LiveData<List<Goal>> getBudgetChallenges();

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    Goal getGoalByIdSync(int id);

    @Query("SELECT * FROM goals WHERE is_budget_challenge = 1 AND budget_month = :month")
    List<Goal> getChallengesForMonth(String month);

    @Query("UPDATE goals SET saved_amount = :amount WHERE id = :id")
    void updateSavedAmount(int id, double amount);

    @Query("UPDATE goals SET current_streak = :streak, longest_streak = :longest WHERE id = :id")
    void updateStreak(int id, int streak, int longest);

    @Query("UPDATE goals SET is_completed = 1 WHERE id = :id")
    void markCompleted(int id);
}
