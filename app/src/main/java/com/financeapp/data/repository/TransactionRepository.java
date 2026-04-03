package com.financeapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.financeapp.data.db.AppDatabase;
import com.financeapp.data.db.TransactionDao;
import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.MonthlyTotal;
import com.financeapp.data.model.Transaction;

import java.util.List;

public class TransactionRepository {

    private final TransactionDao dao;

    public TransactionRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.transactionDao();
    }

    // ── LiveData sources ──────────────────────────────────────────────────

    public LiveData<List<Transaction>> getAllTransactions() {
        return dao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getRecentTransactions(int limit) {
        return dao.getRecentTransactions(limit);
    }

    public LiveData<List<Transaction>> getByType(String type) {
        return dao.getByType(type);
    }

    public LiveData<List<Transaction>> getByCategory(String category) {
        return dao.getByCategory(category);
    }

    public LiveData<List<Transaction>> getByDateRange(long startDate, long endDate) {
        return dao.getByDateRange(startDate, endDate);
    }

    public LiveData<List<Transaction>> search(String query) {
        return dao.search(query);
    }

    public LiveData<Double> getTotalIncome() { return dao.getTotalIncome(); }
    public LiveData<Double> getTotalExpense() { return dao.getTotalExpense(); }
    public LiveData<Double> getBalance() { return dao.getBalance(); }

    public LiveData<List<CategoryTotal>> getExpenseCategoryTotals() {
        return dao.getExpenseCategoryTotals();
    }

    public LiveData<List<CategoryTotal>> getMonthCategoryTotals(long start, long end) {
        return dao.getMonthCategoryTotals(start, end);
    }

    public LiveData<List<MonthlyTotal>> getMonthlyTotals() {
        return dao.getMonthlyTotals();
    }

    public LiveData<Double> getWeekExpense(long start, long end) {
        return dao.getWeekExpense(start, end);
    }

    // ── Sync queries (call on background thread only) ─────────────────────

    public double getMonthIncome(long start, long end) {
        return dao.getMonthIncome(start, end);
    }

    public double getMonthExpense(long start, long end) {
        return dao.getMonthExpense(start, end);
    }

    public double getCategorySpend(String category, long start, long end) {
        return dao.getCategorySpend(category, start, end);
    }

    public double getTotalSpend(long start, long end) {
        return dao.getTotalSpend(start, end);
    }

    public List<CategoryTotal> getExpenseCategoryTotalsSync() {
        return dao.getExpenseCategoryTotalsSync();
    }

    // ── Write operations (always on background thread) ────────────────────

    public void insert(Transaction transaction) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.insert(transaction));
    }

    public void update(Transaction transaction) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.update(transaction));
    }

    public void delete(Transaction transaction) {
        AppDatabase.DB_EXECUTOR.execute(() -> dao.delete(transaction));
    }
}
