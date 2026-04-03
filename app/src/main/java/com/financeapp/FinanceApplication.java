package com.financeapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class FinanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Apply saved theme preference on startup
        boolean darkMode = getSharedPreferences("finance_prefs", MODE_PRIVATE)
                .getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES
                         : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
