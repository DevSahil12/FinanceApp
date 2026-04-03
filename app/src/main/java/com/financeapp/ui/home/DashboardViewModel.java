package com.financeapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.financeapp.data.db.AppDatabase;
import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.SummaryStats;
import com.financeapp.data.model.Transaction;
import com.financeapp.data.repository.TransactionRepository;
import com.financeapp.utils.DateUtils;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    // All LiveData fields initialised in constructor — after repo exists
    private TransactionRepository repo;

    public LiveData<List<Transaction>>   recentTransactions;
    public LiveData<List<CategoryTotal>> categoryTotals;
    public LiveData<Double>              balance;
    public LiveData<Double>              totalIncome;
    public LiveData<Double>              totalExpense;
    public LiveData<Double>              thisWeekExpense;
    public LiveData<Double>              lastWeekExpense;

    private final MutableLiveData<SummaryStats> summaryStats = new MutableLiveData<>();
    public LiveData<SummaryStats> getSummaryStats() { return summaryStats; }

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        // Repo first, LiveData wiring second
        repo = new TransactionRepository(application);

        recentTransactions = repo.getRecentTransactions(5);
        categoryTotals     = repo.getMonthCategoryTotals(
                DateUtils.getMonthStart(), DateUtils.getMonthEnd());
        balance            = repo.getBalance();
        totalIncome        = repo.getTotalIncome();
        totalExpense       = repo.getTotalExpense();
        thisWeekExpense    = repo.getWeekExpense(
                DateUtils.getWeekStart(), System.currentTimeMillis());
        lastWeekExpense    = repo.getWeekExpense(
                DateUtils.getLastWeekStart(), DateUtils.getLastWeekEnd());

        loadSummaryStats();
    }

    private void loadSummaryStats() {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            long mStart = DateUtils.getMonthStart();
            long mEnd   = DateUtils.getMonthEnd();
            long now    = System.currentTimeMillis();

            double monthIncome  = repo.getMonthIncome(mStart, mEnd);
            double monthExpense = repo.getMonthExpense(mStart, mEnd);
            double allIncome    = repo.getMonthIncome(0, now);
            double allExpense   = repo.getMonthExpense(0, now);

            summaryStats.postValue(new SummaryStats(
                    allIncome - allExpense,
                    allIncome, allExpense,
                    monthIncome, monthExpense));
        });
    }

    public void refresh() { loadSummaryStats(); }
}
