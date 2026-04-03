package com.financeapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.financeapp.R;
import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.SummaryStats;
import com.financeapp.databinding.FragmentHomeBinding;
import com.financeapp.ui.transactions.AddEditTransactionSheet;
import com.financeapp.ui.transactions.TransactionAdapter;
import com.financeapp.ui.transactions.TransactionViewModel;
import com.financeapp.utils.CurrencyFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DashboardViewModel viewModel;
    private TransactionAdapter recentAdapter;
    private double latestThisWeek = 0;
    private double latestLastWeek = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        setupGreeting();
        setupRecentList();
        setupPieChart();
        observeData();

        binding.btnSeeAll.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.nav_transactions));
        binding.btnSettings.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.nav_settings));
    }

    private void setupGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting = hour < 12 ? "Good morning 👋"
                        : hour < 17 ? "Good afternoon 👋"
                                    : "Good evening 👋";
        binding.tvGreeting.setText(greeting);
    }

    private void setupRecentList() {
        recentAdapter = new TransactionAdapter();
        binding.rvRecentTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentTransactions.setAdapter(recentAdapter);
        binding.rvRecentTransactions.setHasFixedSize(false);
        recentAdapter.setOnItemClickListener(t -> {
            TransactionViewModel txVm = new ViewModelProvider(requireActivity())
                    .get(TransactionViewModel.class);
            txVm.setEditingTransaction(t);
            new AddEditTransactionSheet().show(getChildFragmentManager(), "edit");
        });
    }

    private void setupPieChart() {
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleRadius(52f);
        binding.pieChart.setTransparentCircleRadius(57f);
        binding.pieChart.setHoleColor(Color.TRANSPARENT);
        binding.pieChart.setDescription(null);
        binding.pieChart.getLegend().setEnabled(true);
        binding.pieChart.getLegend().setTextSize(11f);
        binding.pieChart.getLegend().setWordWrapEnabled(true);
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setRotationEnabled(false);
    }

    private void observeData() {
        viewModel.getSummaryStats().observe(getViewLifecycleOwner(), this::updateSummaryCards);

        viewModel.balance.observe(getViewLifecycleOwner(), bal -> {
            if (bal == null) return;
            binding.tvBalance.setText(CurrencyFormatter.format(requireContext(), Math.abs(bal)));
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), inc -> {
            if (inc == null) return;
            binding.tvTotalIncome.setText(CurrencyFormatter.format(requireContext(), inc));
        });

        viewModel.totalExpense.observe(getViewLifecycleOwner(), exp -> {
            if (exp == null) return;
            binding.tvTotalExpense.setText(CurrencyFormatter.format(requireContext(), exp));
        });

        viewModel.recentTransactions.observe(getViewLifecycleOwner(), list -> {
            boolean empty = list == null || list.isEmpty();
            binding.layoutEmptyTransactions.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvRecentTransactions.setVisibility(empty ? View.GONE  : View.VISIBLE);
            if (!empty) recentAdapter.submitList(list);
        });

        viewModel.categoryTotals.observe(getViewLifecycleOwner(), totals -> {
            boolean empty = totals == null || totals.isEmpty();
            binding.pieChart.setVisibility(empty ? View.GONE    : View.VISIBLE);
            binding.layoutPieEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            if (!empty) updatePieChart(totals);
        });

        viewModel.thisWeekExpense.observe(getViewLifecycleOwner(), val -> {
            if (val == null) return;
            latestThisWeek = val;
            binding.tvThisWeek.setText(CurrencyFormatter.format(requireContext(), val));
            updateWeekDiff();
        });

        viewModel.lastWeekExpense.observe(getViewLifecycleOwner(), val -> {
            if (val == null) return;
            latestLastWeek = val;
            binding.tvLastWeek.setText(CurrencyFormatter.format(requireContext(), val));
            updateWeekDiff();
        });
    }

    private void updateSummaryCards(SummaryStats stats) {
        if (stats == null) return;
        binding.tvMonthIncome.setText(CurrencyFormatter.format(requireContext(), stats.monthIncome));
        binding.tvMonthExpense.setText(CurrencyFormatter.format(requireContext(), stats.monthExpense));
        binding.tvSavingsRate.setText(
                String.format(Locale.getDefault(), "%.0f%%", stats.getSavingsRate()));
    }

    private void updatePieChart(List<CategoryTotal> totals) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer>  colors  = new ArrayList<>();
        for (CategoryTotal ct : totals) {
            if (ct.total <= 0) continue;
            entries.add(new PieEntry((float) ct.total,
                    (ct.categoryIcon != null ? ct.categoryIcon + " " : "") + ct.category));
            colors.add(ct.categoryColor != 0 ? ct.categoryColor : Color.GRAY);
        }
        if (entries.isEmpty()) return;

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(colors);
        ds.setSliceSpace(3f);
        ds.setSelectionShift(6f);
        ds.setValueTextSize(0f);
        binding.pieChart.setData(new PieData(ds));
        binding.pieChart.animateY(800, Easing.EaseInOutQuad);
        binding.pieChart.invalidate();
    }

    private void updateWeekDiff() {
        if (latestLastWeek == 0) {
            binding.tvWeekDiff.setText(latestThisWeek > 0 ? "First week tracked 📊" : "");
            return;
        }
        double pct = ((latestThisWeek - latestLastWeek) / latestLastWeek) * 100;
        String msg;
        int    color;
        if (pct > 5) {
            msg   = String.format(Locale.getDefault(), "📈 %.0f%% more than last week", pct);
            color = requireContext().getColor(R.color.expense_red);
        } else if (pct < -5) {
            msg   = String.format(Locale.getDefault(), "📉 %.0f%% less than last week", Math.abs(pct));
            color = requireContext().getColor(R.color.income_green);
        } else {
            msg   = "➡ Similar to last week";
            color = requireContext().getColor(R.color.text_secondary);
        }
        binding.tvWeekDiff.setText(msg);
        binding.tvWeekDiff.setTextColor(color);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
