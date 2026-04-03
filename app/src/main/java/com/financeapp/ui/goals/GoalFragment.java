package com.financeapp.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.financeapp.databinding.FragmentGoalsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class GoalFragment extends Fragment {

    private FragmentGoalsBinding binding;
    private GoalViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        // ViewPager2 with 2 tabs
        binding.vpGoals.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return GoalListFragment.newInstance(position == 0
                        ? GoalListFragment.TYPE_SAVINGS
                        : GoalListFragment.TYPE_BUDGET);
            }

            @Override
            public int getItemCount() { return 2; }
        });

        new TabLayoutMediator(binding.tabGoals, binding.vpGoals, (tab, pos) ->
                tab.setText(pos == 0 ? "Savings Goals" : "Budget Challenges")
        ).attach();

        binding.fabAddGoal.setOnClickListener(v -> openAddGoalSheet());

        // Refresh budget challenge spend from actual transactions
        viewModel.refreshBudgetChallenges();
    }

    private void openAddGoalSheet() {
        AddGoalSheet sheet = new AddGoalSheet();
        sheet.show(getChildFragmentManager(), "add_goal");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
