package com.financeapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.MonthlyTotal;
import com.financeapp.data.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    // ── CRUD ──────────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    // ── ALL TRANSACTIONS ──────────────────────────────────────────────────

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    LiveData<List<Transaction>> getRecentTransactions(int limit);

    // ── FILTERED QUERIES ──────────────────────────────────────────────────

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<Transaction>> getByType(String type);

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    LiveData<List<Transaction>> getByCategory(String category);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getByDateRange(long startDate, long endDate);

    @Query("SELECT * FROM transactions WHERE " +
           "(note LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%') " +
           "ORDER BY date DESC")
    LiveData<List<Transaction>> search(String query);

    // ── SUMMARY QUERIES ───────────────────────────────────────────────────

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'")
    LiveData<Double> getTotalExpense();

    @Query("SELECT COALESCE(SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END), 0) FROM transactions")
    LiveData<Double> getBalance();

    // Month-specific (pass epoch millis for month start/end)
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME' AND date BETWEEN :start AND :end")
    double getMonthIncome(long start, long end);

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :start AND :end")
    double getMonthExpense(long start, long end);

    // ── CATEGORY TOTALS ───────────────────────────────────────────────────

    @Query("SELECT category, category_icon, category_color, " +
           "SUM(amount) as total, COUNT(*) as transaction_count " +
           "FROM transactions WHERE type = 'EXPENSE' " +
           "GROUP BY category ORDER BY total DESC")
    LiveData<List<CategoryTotal>> getExpenseCategoryTotals();

    @Query("SELECT category, category_icon, category_color, " +
           "SUM(amount) as total, COUNT(*) as transaction_count " +
           "FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :start AND :end " +
           "GROUP BY category ORDER BY total DESC")
    LiveData<List<CategoryTotal>> getMonthCategoryTotals(long start, long end);

    @Query("SELECT category, category_icon, category_color, " +
           "SUM(amount) as total, COUNT(*) as transaction_count " +
           "FROM transactions WHERE type = 'EXPENSE' " +
           "GROUP BY category ORDER BY total DESC")
    List<CategoryTotal> getExpenseCategoryTotalsSync();

    // ── MONTHLY TRENDS ────────────────────────────────────────────────────

    @Query("SELECT " +
           "  strftime('%Y-%m', date/1000, 'unixepoch') as month_key, " +
           "  strftime('%m', date/1000, 'unixepoch') as month_label, " +
           "  SUM(CASE WHEN type='INCOME'  THEN amount ELSE 0 END) as income, " +
           "  SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END) as expense " +
           "FROM transactions " +
           "GROUP BY month_key " +
           "ORDER BY month_key DESC " +
           "LIMIT 6")
    LiveData<List<MonthlyTotal>> getMonthlyTotals();

    // ── BUDGET CHALLENGE HELPERS ──────────────────────────────────────────

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
           "WHERE type = 'EXPENSE' AND category = :category AND date BETWEEN :start AND :end")
    double getCategorySpend(String category, long start, long end);

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
           "WHERE type = 'EXPENSE' AND date BETWEEN :start AND :end")
    double getTotalSpend(long start, long end);

    // ── WEEK COMPARISON ───────────────────────────────────────────────────

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions " +
           "WHERE type = 'EXPENSE' AND date BETWEEN :start AND :end")
    LiveData<Double> getWeekExpense(long start, long end);
}
