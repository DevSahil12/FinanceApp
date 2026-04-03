package com.financeapp.ui.goals;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.financeapp.R;
import com.financeapp.data.model.Goal;
import com.financeapp.databinding.ItemGoalBinding;
import com.financeapp.utils.CurrencyFormatter;
import com.financeapp.utils.DateUtils;

public class GoalAdapter extends ListAdapter<Goal, GoalAdapter.ViewHolder> {

    public interface OnGoalActionListener {
        void onGoalClick(Goal goal);
        void onAddFunds(Goal goal);
    }

    private OnGoalActionListener listener;

    public GoalAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setListener(OnGoalActionListener l) { this.listener = l; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGoalBinding b = ItemGoalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        h.bind(getItem(position));
    }

    public Goal getItemAt(int pos) { return getItem(pos); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGoalBinding b;

        ViewHolder(ItemGoalBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(Goal g) {
            android.content.Context ctx = b.getRoot().getContext();

            b.tvGoalIcon.setText(g.icon != null ? g.icon : "🎯");
            b.tvGoalTitle.setText(g.title);

            String saved  = CurrencyFormatter.format(ctx, g.savedAmount);
            String target = CurrencyFormatter.format(ctx, g.targetAmount);
            b.tvGoalSubtitle.setText(saved + " of " + target);
            b.tvSaved.setText(saved);
            b.tvTarget.setText(" / " + target);

            // Progress bar
            int pct = g.getProgressPercent();
            b.progressBar.setProgress(pct);
            b.tvPercent.setText(pct + "%");

            // Color the progress bar by how close to budget
            if (g.isBudgetChallenge) {
                if (pct >= 100) {
                    b.progressBar.setIndicatorColor(ctx.getColor(R.color.expense_red));
                    b.tvPercent.setTextColor(ctx.getColor(R.color.expense_red));
                } else if (pct >= 80) {
                    b.progressBar.setIndicatorColor(ctx.getColor(R.color.warning_amber));
                    b.tvPercent.setTextColor(ctx.getColor(R.color.warning_amber));
                } else {
                    b.progressBar.setIndicatorColor(ctx.getColor(R.color.income_green));
                    b.tvPercent.setTextColor(ctx.getColor(R.color.income_green));
                }
                // Budget status tag
                b.tvBudgetStatus.setVisibility(View.VISIBLE);
                if (pct >= 100) {
                    b.tvBudgetStatus.setText("⚠ Over budget!");
                    b.tvBudgetStatus.setBackgroundResource(R.drawable.bg_badge_red);
                    b.tvBudgetStatus.setTextColor(ctx.getColor(R.color.expense_red));
                } else {
                    b.tvBudgetStatus.setText("✓ Under budget");
                    b.tvBudgetStatus.setBackgroundResource(R.drawable.bg_badge_green);
                    b.tvBudgetStatus.setTextColor(ctx.getColor(R.color.income_green));
                }
                // Streak badge
                if (g.currentStreak > 0) {
                    b.tvStreak.setVisibility(View.VISIBLE);
                    b.tvStreak.setText("🔥 " + g.currentStreak);
                } else {
                    b.tvStreak.setVisibility(View.GONE);
                }
                b.tvDeadline.setText(g.budgetMonth != null ? g.budgetMonth : "");
                b.btnAddFunds.setVisibility(View.GONE);
            } else {
                // Savings goal
                b.progressBar.setIndicatorColor(
                        g.color != 0 ? g.color : ctx.getColor(R.color.primary));
                b.tvPercent.setTextColor(ctx.getColor(R.color.primary));
                b.tvStreak.setVisibility(View.GONE);
                b.tvBudgetStatus.setVisibility(View.GONE);
                b.btnAddFunds.setVisibility(View.VISIBLE);

                // Deadline
                if (g.deadline > 0) {
                    b.tvDeadline.setText("Due " + DateUtils.formatDate(g.deadline));
                } else {
                    b.tvDeadline.setText("No deadline");
                }

                // Completed badge
                if (g.isCompleted || pct >= 100) {
                    b.tvBudgetStatus.setVisibility(View.VISIBLE);
                    b.tvBudgetStatus.setText("🎉 Completed!");
                    b.tvBudgetStatus.setBackgroundResource(R.drawable.bg_badge_green);
                    b.tvBudgetStatus.setTextColor(ctx.getColor(R.color.income_green));
                }
            }

            b.btnAddFunds.setOnClickListener(v -> {
                if (listener != null) listener.onAddFunds(g);
            });
            b.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onGoalClick(g);
            });
        }
    }

    private static final DiffUtil.ItemCallback<Goal> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Goal>() {
                @Override
                public boolean areItemsTheSame(@NonNull Goal a, @NonNull Goal b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Goal a, @NonNull Goal b) {
                    return a.id == b.id
                            && a.savedAmount == b.savedAmount
                            && a.targetAmount == b.targetAmount
                            && a.currentStreak == b.currentStreak
                            && a.isCompleted == b.isCompleted;
                }
            };
}
