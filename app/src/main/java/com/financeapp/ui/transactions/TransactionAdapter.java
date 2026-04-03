package com.financeapp.ui.transactions;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.financeapp.data.model.Transaction;
import com.financeapp.databinding.ItemTransactionBinding;
import com.financeapp.utils.CurrencyFormatter;
import com.financeapp.utils.DateUtils;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    private OnItemClickListener listener;

    public TransactionAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public Transaction getItemAt(int position) {
        return getItem(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionBinding b;

        ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(Transaction t) {
            // Icon & background
            b.tvIcon.setText(t.categoryIcon != null ? t.categoryIcon : "💸");
            b.cvIcon.setCardBackgroundColor(
                    t.categoryColor != 0 ? alphaColor(t.categoryColor, 0.15f)
                                         : Color.parseColor("#1A1E88E5"));

            // Category name
            b.tvCategory.setText(t.category);

            // Date label
            b.tvDate.setText(DateUtils.getRelativeDate(t.date));

            // Note (only show if non-empty)
            if (t.note != null && !t.note.trim().isEmpty()) {
                b.tvNote.setText("· " + t.note);
                b.tvNote.setVisibility(android.view.View.VISIBLE);
            } else {
                b.tvNote.setVisibility(android.view.View.GONE);
            }

            // Amount
            String ctx = b.getRoot().getContext().toString();
            double amt = t.amount;
            if (t.isExpense()) {
                b.tvAmount.setText("- " + CurrencyFormatter.format(b.getRoot().getContext(), amt));
                b.tvAmount.setTextColor(b.getRoot().getContext().getColor(
                        com.financeapp.R.color.expense_red));
            } else {
                b.tvAmount.setText("+ " + CurrencyFormatter.format(b.getRoot().getContext(), amt));
                b.tvAmount.setTextColor(b.getRoot().getContext().getColor(
                        com.financeapp.R.color.income_green));
            }

            // Click
            b.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(t);
            });
        }
    }

    private int alphaColor(int color, float alpha) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb((int)(alpha * 255), r, g, b);
    }

    private static final DiffUtil.ItemCallback<Transaction> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Transaction>() {
                @Override
                public boolean areItemsTheSame(@NonNull Transaction a, @NonNull Transaction b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Transaction a, @NonNull Transaction b) {
                    return a.id == b.id
                            && a.amount == b.amount
                            && a.type.equals(b.type)
                            && a.category.equals(b.category)
                            && a.date == b.date;
                }
            };
}
