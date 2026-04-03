package com.financeapp.ui.goals;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.financeapp.R;
import com.financeapp.data.model.Goal;
import com.financeapp.databinding.BottomSheetAddGoalBinding;
import com.financeapp.utils.CategoryManager;
import com.financeapp.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;

public class AddGoalSheet extends BottomSheetDialogFragment {

    private BottomSheetAddGoalBinding binding;
    private GoalViewModel viewModel;

    private boolean isBudgetChallenge = false;
    private long selectedDeadline = 0;
    private String selectedIcon = "🎯";

    private static final String[] GOAL_ICONS =
            {"🎯", "✈️", "🏠", "🚗", "💍", "📱", "🎓", "🏋️", "🌴", "💊", "🎮", "📚"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog d = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        d.setOnShowListener(dialog -> {
            BottomSheetBehavior<?> b = ((BottomSheetDialog) dialog).getBehavior();
            b.setState(BottomSheetBehavior.STATE_EXPANDED);
            b.setSkipCollapsed(true);
        });
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddGoalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        setupTabs();
        setupIconPicker();
        setupBudgetCategoryDropdown();
        setupDatePicker();

        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnSaveGoal.setOnClickListener(v -> saveGoal());
    }

    private void setupTabs() {
        binding.tabGoalType.addTab(binding.tabGoalType.newTab().setText("Savings Goal"));
        binding.tabGoalType.addTab(binding.tabGoalType.newTab().setText("Budget Challenge"));

        binding.tabGoalType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                isBudgetChallenge = tab.getPosition() == 1;
                binding.tilDeadline.setVisibility(isBudgetChallenge ? View.GONE : View.VISIBLE);
                binding.tilBudgetCategory.setVisibility(isBudgetChallenge ? View.VISIBLE : View.GONE);
                binding.tilTarget.setHint(isBudgetChallenge
                        ? getString(R.string.budget_limit)
                        : getString(R.string.target_amount));
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupIconPicker() {
        int sizeDp = (int)(56 * getResources().getDisplayMetrics().density);
        int padDp  = (int)(8  * getResources().getDisplayMetrics().density);
        int marDp  = (int)(6  * getResources().getDisplayMetrics().density);

        for (String icon : GOAL_ICONS) {
            TextView tv = new TextView(requireContext());
            tv.setText(icon);
            tv.setTextSize(24);
            tv.setGravity(android.view.Gravity.CENTER);
            tv.setBackground(requireContext()
                    .getDrawable(R.drawable.bg_icon_button));
            tv.setPadding(padDp, padDp, padDp, padDp);
            ViewGroup.MarginLayoutParams lp =
                    new ViewGroup.MarginLayoutParams(sizeDp, sizeDp);
            lp.setMarginEnd(marDp);
            tv.setLayoutParams(lp);

            tv.setOnClickListener(v -> {
                selectedIcon = icon;
                // Highlight selected
                for (int i = 0; i < binding.iconPickerRow.getChildCount(); i++) {
                    View child = binding.iconPickerRow.getChildAt(i);
                    child.setBackgroundResource(R.drawable.bg_icon_button);
                }
                v.setBackgroundResource(R.drawable.bg_icon_button_selected);
            });
            binding.iconPickerRow.addView(tv);
        }

        // Select first by default
        if (binding.iconPickerRow.getChildCount() > 0) {
            binding.iconPickerRow.getChildAt(0)
                    .setBackgroundResource(R.drawable.bg_icon_button_selected);
        }
    }

    private void setupBudgetCategoryDropdown() {
        String[] options = CategoryManager.getBudgetCategoryOptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                options);
        binding.actvBudgetCategory.setAdapter(adapter);
        binding.actvBudgetCategory.setText(options[0], false);
    }

    private void setupDatePicker() {
        View.OnClickListener open = v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText("Select deadline")
                    .build();
            picker.addOnPositiveButtonClickListener(sel -> {
                selectedDeadline = sel;
                binding.etDeadline.setText(DateUtils.formatDate(sel));
            });
            picker.show(getChildFragmentManager(), "deadline_picker");
        };
        binding.etDeadline.setOnClickListener(open);
        binding.tilDeadline.setEndIconOnClickListener(open);
    }

    private void saveGoal() {
        String title = binding.etGoalTitle.getText() != null
                ? binding.etGoalTitle.getText().toString().trim() : "";
        if (title.isEmpty()) {
            binding.tilGoalTitle.setError(getString(R.string.error_title_empty));
            return;
        }
        binding.tilGoalTitle.setError(null);

        String targetStr = binding.etTarget.getText() != null
                ? binding.etTarget.getText().toString().trim() : "";
        if (targetStr.isEmpty()) {
            binding.tilTarget.setError(getString(R.string.error_target_empty));
            return;
        }
        double target;
        try {
            target = Double.parseDouble(targetStr);
        } catch (NumberFormatException e) {
            binding.tilTarget.setError(getString(R.string.error_amount_invalid));
            return;
        }
        binding.tilTarget.setError(null);

        int color = Color.parseColor("#1E88E5");

        if (isBudgetChallenge) {
            String rawCat = binding.actvBudgetCategory.getText().toString().trim();
            String budgetCat = rawCat.equals("All Categories") ? null : rawCat;
            String month = DateUtils.getCurrentMonthKey();
            Goal goal = new Goal(title, target, budgetCat, month, selectedIcon, color);
            viewModel.insert(goal);
        } else {
            Goal goal = new Goal(title, target, selectedDeadline, selectedIcon, color);
            viewModel.insert(goal);
        }

        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
