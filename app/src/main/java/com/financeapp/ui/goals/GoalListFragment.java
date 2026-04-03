package com.financeapp.ui.goals;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.financeapp.R;
import com.financeapp.data.model.Goal;
import com.financeapp.databinding.FragmentGoalListBinding;
import com.google.android.material.snackbar.Snackbar;

public class GoalListFragment extends Fragment {

    public static final String TYPE_SAVINGS = "SAVINGS";
    public static final String TYPE_BUDGET  = "BUDGET";
    private static final String ARG_TYPE    = "type";

    private FragmentGoalListBinding binding;
    private GoalViewModel viewModel;
    private GoalAdapter adapter;
    private String listType;

    public static GoalListFragment newInstance(String type) {
        GoalListFragment f = new GoalListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listType = getArguments() != null
                ? getArguments().getString(ARG_TYPE, TYPE_SAVINGS) : TYPE_SAVINGS;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGoalListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        adapter = new GoalAdapter();
        binding.rvGoals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGoals.setAdapter(adapter);

        adapter.setListener(new GoalAdapter.OnGoalActionListener() {
            @Override
            public void onGoalClick(Goal goal) {
                showGoalOptions(goal);
            }

            @Override
            public void onAddFunds(Goal goal) {
                showAddFundsDialog(goal);
            }
        });

        observeData();
    }

    private void observeData() {
        if (TYPE_SAVINGS.equals(listType)) {
            viewModel.savingsGoals.observe(getViewLifecycleOwner(), goals -> {
                updateList(goals);
            });
        } else {
            viewModel.budgetChallenges.observe(getViewLifecycleOwner(), goals -> {
                updateList(goals);
            });
        }

        viewModel.getSnackMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty() && getView() != null) {
                Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void updateList(java.util.List<Goal> goals) {
        if (goals == null || goals.isEmpty()) {
            binding.layoutEmptyGoals.setVisibility(View.VISIBLE);
            binding.rvGoals.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyGoals.setVisibility(View.GONE);
            binding.rvGoals.setVisibility(View.VISIBLE);
            adapter.submitList(goals);
        }
    }

    private void showAddFundsDialog(Goal goal) {
        EditText input = new EditText(requireContext());
        input.setHint("Amount (₹)");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add to " + goal.title)
                .setView(input)
                .setPositiveButton("Add", (d, w) -> {
                    String txt = input.getText().toString().trim();
                    if (!txt.isEmpty()) {
                        try {
                            double amt = Double.parseDouble(txt);
                            viewModel.addToSavings(goal.id, amt);
                        } catch (NumberFormatException ignored) {}
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showGoalOptions(Goal goal) {
        String[] options = {"Delete Goal"};
        new AlertDialog.Builder(requireContext())
                .setTitle(goal.title)
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        viewModel.delete(goal);
                        Snackbar.make(binding.getRoot(), "Goal deleted", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
