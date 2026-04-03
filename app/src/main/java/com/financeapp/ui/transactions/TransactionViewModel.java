package com.financeapp.ui.transactions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.financeapp.data.model.Transaction;
import com.financeapp.data.repository.TransactionRepository;
import com.financeapp.utils.DateUtils;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    public static final String FILTER_ALL      = "ALL";
    public static final String FILTER_INCOME   = "INCOME";
    public static final String FILTER_EXPENSE  = "EXPENSE";
    public static final String FILTER_SEARCH   = "SEARCH";
    public static final String FILTER_CATEGORY = "CATEGORY";
    public static final String FILTER_DATE     = "DATE";

    // Not final — must be assigned in constructor before switchMap wires up
    private TransactionRepository repo;
    public LiveData<List<Transaction>> transactions;

    private final MutableLiveData<FilterState> filterState = new MutableLiveData<>(new FilterState());
    private final MutableLiveData<Transaction> editingTransaction = new MutableLiveData<>(null);

    public LiveData<Transaction> getEditingTransaction() { return editingTransaction; }

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        // IMPORTANT: repo must be initialised before Transformations.switchMap()
        // because switchMap calls the lambda immediately with the current filterState value.
        repo = new TransactionRepository(application);
        transactions = Transformations.switchMap(filterState, this::queryForFilter);
    }

    private LiveData<List<Transaction>> queryForFilter(FilterState state) {
        switch (state.filterType) {
            case FILTER_INCOME:   return repo.getByType("INCOME");
            case FILTER_EXPENSE:  return repo.getByType("EXPENSE");
            case FILTER_CATEGORY: return repo.getByCategory(state.category);
            case FILTER_SEARCH:   return repo.search(state.searchQuery);
            case FILTER_DATE:     return repo.getByDateRange(state.startDate, state.endDate);
            default:              return repo.getAllTransactions();
        }
    }

    public void setFilterAll()                      { filterState.setValue(new FilterState()); }
    public void setFilterIncome()                   { filterState.setValue(new FilterState(FILTER_INCOME)); }
    public void setFilterExpense()                  { filterState.setValue(new FilterState(FILTER_EXPENSE)); }
    public void setFilterCategory(String category)  { filterState.setValue(new FilterState(FILTER_CATEGORY, category)); }
    public void setSearch(String query)             { filterState.setValue(new FilterState(FILTER_SEARCH, query)); }
    public void setDateRange(long start, long end)  { filterState.setValue(new FilterState(start, end)); }
    public void setThisMonth()                      { setDateRange(DateUtils.getMonthStart(), DateUtils.getMonthEnd()); }

    public void setEditingTransaction(Transaction t) { editingTransaction.setValue(t); }
    public void clearEditing()                       { editingTransaction.setValue(null); }

    public void insert(Transaction t) { repo.insert(t); }
    public void update(Transaction t) { repo.update(t); }
    public void delete(Transaction t) { repo.delete(t); }

    public FilterState getCurrentFilter() { return filterState.getValue(); }

    // ── FilterState ────────────────────────────────────────────────────────

    public static class FilterState {
        public final String filterType;
        public final String category;
        public final String searchQuery;
        public final long   startDate;
        public final long   endDate;

        public FilterState() {
            this.filterType  = FILTER_ALL;
            this.category    = null;
            this.searchQuery = null;
            this.startDate   = 0;
            this.endDate     = 0;
        }

        // Single-arg: filterType (INCOME / EXPENSE)
        public FilterState(String filterType) {
            this.filterType  = filterType;
            this.category    = null;
            this.searchQuery = null;
            this.startDate   = 0;
            this.endDate     = 0;
        }

        // Two-arg: filterType + value (used for CATEGORY and SEARCH)
        public FilterState(String filterType, String value) {
            this.filterType  = filterType;
            if (FILTER_SEARCH.equals(filterType)) {
                this.searchQuery = value;
                this.category    = null;
            } else {
                this.category    = value;
                this.searchQuery = null;
            }
            this.startDate = 0;
            this.endDate   = 0;
        }

        // Date range
        public FilterState(long startDate, long endDate) {
            this.filterType  = FILTER_DATE;
            this.startDate   = startDate;
            this.endDate     = endDate;
            this.category    = null;
            this.searchQuery = null;
        }
    }
}
