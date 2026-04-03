package com.financeapp.utils;

import android.graphics.Color;

import com.financeapp.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager {

    private static List<Category> expenseCategories;
    private static List<Category> incomeCategories;

    public static List<Category> getExpenseCategories() {
        if (expenseCategories == null) {
            expenseCategories = new ArrayList<>();
            expenseCategories.add(new Category("Food",          "🍔", Color.parseColor("#FF5722"), "EXPENSE"));
            expenseCategories.add(new Category("Transport",     "🚌", Color.parseColor("#FF9800"), "EXPENSE"));
            expenseCategories.add(new Category("Shopping",      "🛍️", Color.parseColor("#E91E63"), "EXPENSE"));
            expenseCategories.add(new Category("Bills",         "📄", Color.parseColor("#607D8B"), "EXPENSE"));
            expenseCategories.add(new Category("Entertainment", "🎬", Color.parseColor("#9C27B0"), "EXPENSE"));
            expenseCategories.add(new Category("Health",        "💊", Color.parseColor("#00BCD4"), "EXPENSE"));
            expenseCategories.add(new Category("Education",     "📚", Color.parseColor("#3F51B5"), "EXPENSE"));
            expenseCategories.add(new Category("Travel",        "✈️", Color.parseColor("#009688"), "EXPENSE"));
            expenseCategories.add(new Category("Fitness",       "🏋️", Color.parseColor("#4CAF50"), "EXPENSE"));
            expenseCategories.add(new Category("Other",         "💸", Color.parseColor("#795548"), "EXPENSE"));
        }
        return expenseCategories;
    }

    public static List<Category> getIncomeCategories() {
        if (incomeCategories == null) {
            incomeCategories = new ArrayList<>();
            incomeCategories.add(new Category("Salary",     "💼", Color.parseColor("#4CAF50"), "INCOME"));
            incomeCategories.add(new Category("Freelance",  "💻", Color.parseColor("#2196F3"), "INCOME"));
            incomeCategories.add(new Category("Business",   "🏪", Color.parseColor("#FF9800"), "INCOME"));
            incomeCategories.add(new Category("Investment", "📈", Color.parseColor("#8BC34A"), "INCOME"));
            incomeCategories.add(new Category("Gift",       "🎁", Color.parseColor("#E91E63"), "INCOME"));
            incomeCategories.add(new Category("Refund",     "↩️", Color.parseColor("#00BCD4"), "INCOME"));
            incomeCategories.add(new Category("Other",      "💰", Color.parseColor("#9C27B0"), "INCOME"));
        }
        return incomeCategories;
    }

    public static List<Category> getAllCategories() {
        List<Category> all = new ArrayList<>();
        all.addAll(getExpenseCategories());
        all.addAll(getIncomeCategories());
        return all;
    }

    public static Category findByName(String name, String type) {
        List<Category> list = "INCOME".equals(type) ? getIncomeCategories() : getExpenseCategories();
        for (Category c : list) {
            if (c.name.equals(name)) return c;
        }
        return new Category(name, "💸", Color.parseColor("#795548"), type);
    }

    public static String[] getExpenseCategoryNames() {
        List<Category> cats = getExpenseCategories();
        String[] names = new String[cats.size()];
        for (int i = 0; i < cats.size(); i++) names[i] = cats.get(i).name;
        return names;
    }

    public static String[] getIncomeCategoryNames() {
        List<Category> cats = getIncomeCategories();
        String[] names = new String[cats.size()];
        for (int i = 0; i < cats.size(); i++) names[i] = cats.get(i).name;
        return names;
    }

    // For budget challenge - all expense category names + "All Categories"
    public static String[] getBudgetCategoryOptions() {
        List<Category> cats = getExpenseCategories();
        String[] names = new String[cats.size() + 1];
        names[0] = "All Categories";
        for (int i = 0; i < cats.size(); i++) names[i + 1] = cats.get(i).name;
        return names;
    }
}
