package com.financeapp.ui.insights;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.financeapp.R;
import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.MonthlyTotal;
import com.financeapp.databinding.FragmentInsightsBinding;
import com.financeapp.utils.CurrencyFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class InsightsFragment extends Fragment {

    private FragmentInsightsBinding binding;
    private InsightsViewModel viewModel;

    // Hold latest values so pie can be drawn once both arrive
    private double latestMonthIncome  = 0;
    private double latestMonthExpense = 0;
    private double latestThisWeek     = 0;
    private double latestLastWeek     = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InsightsViewModel.class);
        configureCharts();
        observeData();
    }

    // ── Chart configuration ───────────────────────────────────────────────

    private void configureCharts() {
        // Income vs Expense donut
        binding.pieIncomeExpense.setDrawHoleEnabled(true);
        binding.pieIncomeExpense.setHoleRadius(50f);
        binding.pieIncomeExpense.setDescription(null);
        binding.pieIncomeExpense.getLegend().setEnabled(false);
        binding.pieIncomeExpense.setDrawEntryLabels(false);
        binding.pieIncomeExpense.setRotationEnabled(false);

        // Category horizontal bar
        binding.barChartCategories.setDescription(null);
        binding.barChartCategories.getLegend().setEnabled(false);
        binding.barChartCategories.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChartCategories.getXAxis().setDrawGridLines(false);
        binding.barChartCategories.getAxisRight().setEnabled(false);
        binding.barChartCategories.setFitBars(true);

        // 6-month line
        binding.lineChartTrend.setDescription(null);
        binding.lineChartTrend.getLegend().setEnabled(true);
        binding.lineChartTrend.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.lineChartTrend.getXAxis().setDrawGridLines(false);
        binding.lineChartTrend.getAxisRight().setEnabled(false);

        // Week bar
        binding.barChartWeek.setDescription(null);
        binding.barChartWeek.getLegend().setEnabled(false);
        binding.barChartWeek.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChartWeek.getXAxis().setDrawGridLines(false);
        binding.barChartWeek.getAxisRight().setEnabled(false);
    }

    // ── Observers ─────────────────────────────────────────────────────────

    private void observeData() {
        viewModel.getTopInsight().observe(getViewLifecycleOwner(),
                s -> { if (s != null) binding.tvSmartInsight.setText(s); });

        // Monthly totals → income/expense pie + line chart
        viewModel.monthlyTotals.observe(getViewLifecycleOwner(), monthly -> {
            if (monthly == null || monthly.isEmpty()) return;
            MonthlyTotal latest = monthly.get(0); // DESC order → most recent first
            latestMonthIncome  = latest.income;
            latestMonthExpense = latest.expense;
            drawIncomeExpensePie();
            drawLineChart(monthly);
        });

        // All-time category totals → horizontal bar
        viewModel.expenseCategoryTotals.observe(getViewLifecycleOwner(), totals -> {
            if (totals == null || totals.isEmpty()) {
                binding.barChartCategories.setVisibility(View.GONE);
                binding.layoutNoData.setVisibility(View.VISIBLE);
            } else {
                binding.barChartCategories.setVisibility(View.VISIBLE);
                binding.layoutNoData.setVisibility(View.GONE);
                drawCategoryBarChart(totals);
            }
        });

        // Week comparison
        viewModel.thisWeekExpense.observe(getViewLifecycleOwner(), val -> {
            if (val == null) return;
            latestThisWeek = val;
            drawWeekChart();
        });

        viewModel.lastWeekExpense.observe(getViewLifecycleOwner(), val -> {
            if (val == null) return;
            latestLastWeek = val;
            drawWeekChart();
        });
    }

    // ── Chart drawing ─────────────────────────────────────────────────────

    private void drawIncomeExpensePie() {
        if (latestMonthIncome == 0 && latestMonthExpense == 0) return;

        List<PieEntry> entries = new ArrayList<>();
        if (latestMonthIncome  > 0) entries.add(new PieEntry((float) latestMonthIncome,  "Income"));
        if (latestMonthExpense > 0) entries.add(new PieEntry((float) latestMonthExpense, "Expense"));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(
                requireContext().getColor(R.color.income_green),
                requireContext().getColor(R.color.expense_red));
        ds.setSliceSpace(3f);
        ds.setValueTextSize(0f);

        binding.pieIncomeExpense.setData(new PieData(ds));
        binding.pieIncomeExpense.animateY(600, Easing.EaseInOutQuad);
        binding.pieIncomeExpense.invalidate();

        binding.tvIncomeLegend.setText("Income  "
                + CurrencyFormatter.format(requireContext(), latestMonthIncome));
        binding.tvExpenseLegend.setText("Expense  "
                + CurrencyFormatter.format(requireContext(), latestMonthExpense));
    }

    private void drawCategoryBarChart(List<CategoryTotal> totals) {
        int count = Math.min(totals.size(), 6);
        List<BarEntry> entries = new ArrayList<>();
        List<Integer>  colors  = new ArrayList<>();
        String[]       labels  = new String[count];

        for (int i = 0; i < count; i++) {
            CategoryTotal ct = totals.get(i);
            entries.add(new BarEntry(i, (float) ct.total));
            colors.add(ct.categoryColor != 0 ? ct.categoryColor : Color.GRAY);
            labels[i] = (ct.categoryIcon != null ? ct.categoryIcon + " " : "") + ct.category;
        }

        BarDataSet ds = new BarDataSet(entries, "Spending");
        ds.setColors(colors);
        ds.setValueTextSize(10f);
        ds.setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float v) {
                return CurrencyFormatter.formatShort(requireContext(), v);
            }
        });

        binding.barChartCategories.getXAxis()
                .setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.barChartCategories.getXAxis().setLabelCount(count);
        binding.barChartCategories.getXAxis().setTextSize(10f);
        binding.barChartCategories.setData(new BarData(ds));
        binding.barChartCategories.animateY(700);
        binding.barChartCategories.invalidate();
    }

    private void drawLineChart(List<MonthlyTotal> monthly) {
        // Reverse DESC list to chronological order
        List<MonthlyTotal> ordered = new ArrayList<>(monthly);
        Collections.reverse(ordered);

        List<Entry> incEntries = new ArrayList<>();
        List<Entry> expEntries = new ArrayList<>();
        String[]    labels     = new String[ordered.size()];

        for (int i = 0; i < ordered.size(); i++) {
            MonthlyTotal m = ordered.get(i);
            incEntries.add(new Entry(i, (float) m.income));
            expEntries.add(new Entry(i, (float) m.expense));
            // monthLabel is the 2-digit month from SQL; convert "01".."12" → "Jan".."Dec"
            labels[i] = monthName(m.monthLabel, m.monthKey);
        }

        LineDataSet incDs = buildLine(incEntries, "Income",
                requireContext().getColor(R.color.income_green));
        LineDataSet expDs = buildLine(expEntries, "Expense",
                requireContext().getColor(R.color.expense_red));

        binding.lineChartTrend.getXAxis()
                .setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.lineChartTrend.getXAxis().setLabelCount(labels.length);
        binding.lineChartTrend.setData(new LineData(incDs, expDs));
        binding.lineChartTrend.animateX(800);
        binding.lineChartTrend.invalidate();
    }

    private LineDataSet buildLine(List<Entry> entries, String label, int color) {
        LineDataSet ds = new LineDataSet(entries, label);
        ds.setColor(color);
        ds.setCircleColor(color);
        ds.setLineWidth(2.5f);
        ds.setCircleRadius(4f);
        ds.setDrawValues(false);
        ds.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ds.setDrawFilled(true);
        ds.setFillAlpha(20);
        ds.setFillColor(color);
        return ds;
    }

    private void drawWeekChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) latestLastWeek));
        entries.add(new BarEntry(1, (float) latestThisWeek));

        BarDataSet ds = new BarDataSet(entries, "");
        ds.setColors(Color.parseColor("#B0BEC5"),
                requireContext().getColor(R.color.primary));
        ds.setValueTextSize(11f);
        ds.setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float v) {
                return CurrencyFormatter.formatShort(requireContext(), v);
            }
        });

        binding.barChartWeek.getXAxis()
                .setValueFormatter(new IndexAxisValueFormatter(
                        new String[]{"Last Week", "This Week"}));
        binding.barChartWeek.getXAxis().setLabelCount(2);
        binding.barChartWeek.setData(new BarData(ds));
        binding.barChartWeek.animateY(600);
        binding.barChartWeek.invalidate();

        // Insight text
        if (latestLastWeek > 0) {
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
                msg   = "➡ Similar spending to last week";
                color = requireContext().getColor(R.color.text_secondary);
            }
            binding.tvWeekInsight.setText(msg);
            binding.tvWeekInsight.setTextColor(color);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static final String[] MONTH_NAMES =
            {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    private String monthName(String twoDigit, String yearMonth) {
        if (twoDigit != null && twoDigit.length() == 2) {
            try {
                int idx = Integer.parseInt(twoDigit) - 1;
                if (idx >= 0 && idx < 12) return MONTH_NAMES[idx];
            } catch (NumberFormatException ignored) {}
        }
        // Fallback: last 3 chars of year-month key
        if (yearMonth != null && yearMonth.length() >= 7)
            return yearMonth.substring(5);
        return yearMonth != null ? yearMonth : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
