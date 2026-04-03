package com.financeapp.ui.transactions;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.financeapp.R;
import com.financeapp.data.model.Transaction;
import com.financeapp.databinding.FragmentTransactionsBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TransactionListFragment extends Fragment {

    private FragmentTransactionsBinding binding;
    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        setupRecyclerView();
        setupSwipeToDelete();
        setupSearch();
        setupFilters();
        observeData();

        binding.fabAdd.setOnClickListener(v -> openAddSheet(null));
    }

    private void setupRecyclerView() {
        adapter = new TransactionAdapter();
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);
        adapter.setOnItemClickListener(this::openAddSheet);
    }

    private void setupSwipeToDelete() {
        ColorDrawable redBg = new ColorDrawable(Color.parseColor("#FFEBEE"));

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                         @NonNull RecyclerView.ViewHolder vh,
                                         @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                           @NonNull RecyclerView recyclerView,
                                           @NonNull RecyclerView.ViewHolder viewHolder,
                                           float dX, float dY,
                                           int actionState, boolean isCurrentlyActive) {
                        View itemView = viewHolder.itemView;
                        redBg.setBounds(
                                itemView.getRight() + (int) dX,
                                itemView.getTop(),
                                itemView.getRight(),
                                itemView.getBottom());
                        redBg.draw(c);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                                actionState, isCurrentlyActive);
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int dir) {
                        int pos = viewHolder.getAdapterPosition();
                        Transaction deleted = adapter.getItemAt(pos);
                        viewModel.delete(deleted);

                        Snackbar.make(binding.getRoot(),
                                        getString(R.string.transaction_deleted),
                                        Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.undo), v -> viewModel.insert(deleted))
                                .show();
                    }
                };

        new ItemTouchHelper(callback).attachToRecyclerView(binding.rvTransactions);
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(
                new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String q) {
                        viewModel.setSearch(q);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String q) {
                        if (q.isEmpty()) {
                            viewModel.setFilterAll();
                            binding.chipAll.setChecked(true);
                        } else {
                            viewModel.setSearch(q);
                        }
                        return true;
                    }
                });
    }

    private void setupFilters() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, ids) -> {
            if (ids.isEmpty()) return;
            int id = ids.get(0);
            if (id == R.id.chip_all)        viewModel.setFilterAll();
            else if (id == R.id.chip_income)  viewModel.setFilterIncome();
            else if (id == R.id.chip_expense) viewModel.setFilterExpense();
            else if (id == R.id.chip_this_month) viewModel.setThisMonth();
        });
    }

    private void observeData() {
        viewModel.transactions.observe(getViewLifecycleOwner(), transactions -> {
            if (transactions == null || transactions.isEmpty()) {
                binding.layoutEmpty.setVisibility(View.VISIBLE);
                binding.rvTransactions.setVisibility(View.GONE);
                binding.tvCount.setText("No transactions");
            } else {
                binding.layoutEmpty.setVisibility(View.GONE);
                binding.rvTransactions.setVisibility(View.VISIBLE);
                adapter.submitList(transactions);
                binding.tvCount.setText(transactions.size() + " transaction"
                        + (transactions.size() == 1 ? "" : "s"));
            }
        });
    }

    private void openAddSheet(@Nullable Transaction existing) {
        viewModel.setEditingTransaction(existing);
        AddEditTransactionSheet sheet = new AddEditTransactionSheet();
        sheet.show(getChildFragmentManager(), "add_transaction");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
