package com.financeapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "target_amount")
    public double targetAmount;

    @ColumnInfo(name = "saved_amount")
    public double savedAmount;

    @ColumnInfo(name = "deadline")
    public long deadline; // epoch millis, 0 = no deadline

    @ColumnInfo(name = "icon")
    public String icon; // emoji string

    @ColumnInfo(name = "color")
    public int color;

    @ColumnInfo(name = "is_completed")
    public boolean isCompleted;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    // Budget challenge fields
    @ColumnInfo(name = "is_budget_challenge")
    public boolean isBudgetChallenge;

    @ColumnInfo(name = "budget_category")
    public String budgetCategory; // null = all categories

    @ColumnInfo(name = "budget_month")
    public String budgetMonth; // "2024-01" format

    @ColumnInfo(name = "current_streak")
    public int currentStreak;

    @ColumnInfo(name = "longest_streak")
    public int longestStreak;

    public Goal() {}

    // Savings goal constructor
    public Goal(String title, double targetAmount, long deadline, String icon, int color) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
        this.icon = icon;
        this.color = color;
        this.savedAmount = 0;
        this.isCompleted = false;
        this.isBudgetChallenge = false;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.createdAt = System.currentTimeMillis();
    }

    // Budget challenge constructor
    public Goal(String title, double targetAmount, String budgetCategory,
                String budgetMonth, String icon, int color) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.budgetCategory = budgetCategory;
        this.budgetMonth = budgetMonth;
        this.icon = icon;
        this.color = color;
        this.savedAmount = 0;
        this.isCompleted = false;
        this.isBudgetChallenge = true;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.createdAt = System.currentTimeMillis();
    }

    public double getProgress() {
        if (targetAmount == 0) return 0;
        return Math.min(savedAmount / targetAmount, 1.0);
    }

    public int getProgressPercent() {
        return (int) (getProgress() * 100);
    }
}
