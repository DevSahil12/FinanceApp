package com.financeapp.ui.insights;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.MonthlyTotal;
import com.financeapp.data.repository.TransactionRepository;
import com.financeapp.utils.DateUtils;

import java.util.List;

public class InsightsViewModel extends AndroidViewModel {

    private final TransactionRepository repo;

    public final LiveData<List<CategoryTotal>> expenseCategoryTotals;
    public final LiveData<List<MonthlyTotal>>  monthlyTotals;
    public final LiveData<Double>              thisWeekExpense;
    public final LiveData<Double>              lastWeekExpense;
    public final LiveData<List<CategoryTotal>> monthCategoryTotals;

    private final MutableLiveData<String> topInsight = new MutableLiveData<>();
    public LiveData<String> getTopInsight() { return topInsight; }

    public InsightsViewModel(@NonNull Application application) {
        super(application);
        repo = new TransactionRepository(application);

        expenseCategoryTotals = repo.getExpenseCategoryTotals();
        monthlyTotals         = repo.getMonthlyTotals();
        thisWeekExpense       = repo.getWeekExpense(DateUtils.getWeekStart(), System.currentTimeMillis());
        lastWeekExpense       = repo.getWeekExpense(DateUtils.getLastWeekStart(), DateUtils.getLastWeekEnd());
        monthCategoryTotals   = repo.getMonthCategoryTotals(DateUtils.getMonthStart(), DateUtils.getMonthEnd());

        computeTopInsight();
    }

    private void computeTopInsight() {
        com.financeapp.data.db.AppDatabase.DB_EXECUTOR.execute(() -> {
            List<CategoryTotal> totals = repo.getExpenseCategoryTotalsSync();
            if (totals == null || totals.isEmpty()) {
                topInsight.postValue("Add some transactions to see insights.");
                return;
            }
            CategoryTotal top = totals.get(0);
            // Compare this month vs last month for top category
            long thisStart = DateUtils.getMonthStart();
            long thisEnd   = DateUtils.getMonthEnd();
            long lastStart = thisStart - 30L * 24 * 60 * 60 * 1000;
            long lastEnd   = thisStart - 1;

            double thisMonthAmt = repo.getCategorySpend(top.category, thisStart, thisEnd);
            double lastMonthAmt = repo.getCategorySpend(top.category, lastStart, lastEnd);

            String insight;
            if (lastMonthAmt == 0) {
                insight = top.categoryIcon + " Your top spending is " + top.category + " this month.";
            } else {
                double pct = ((thisMonthAmt - lastMonthAmt) / lastMonthAmt) * 100;
                if (pct > 10) {
                    insight = top.categoryIcon + " " + top.category + " spending is up " +
                              String.format("%.0f%%", pct) + " vs last month.";
                } else if (pct < -10) {
                    insight = top.categoryIcon + " Great! " + top.category + " spending is down " +
                              String.format("%.0f%%", Math.abs(pct)) + " vs last month.";
                } else {
                    insight = top.categoryIcon + " " + top.category + " is your highest spend category.";
                }
            }
            topInsight.postValue(insight);
        });
    }
}
