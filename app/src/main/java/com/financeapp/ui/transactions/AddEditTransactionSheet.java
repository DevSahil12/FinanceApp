package com.financeapp.ui.transactions;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.financeapp.R;
import com.financeapp.data.model.Category;
import com.financeapp.data.model.Transaction;
import com.financeapp.databinding.BottomSheetAddTransactionBinding;
import com.financeapp.utils.CategoryManager;
import com.financeapp.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;

public class AddEditTransactionSheet extends BottomSheetDialogFragment {

    private BottomSheetAddTransactionBinding binding;
    private TransactionViewModel viewModel;
    private Transaction editingTransaction;
    private long selectedDate = System.currentTimeMillis();
    private String selectedType = "EXPENSE";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            BottomSheetBehavior<?> behavior = ((BottomSheetDialog) d).getBehavior();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        setupTypeToggle();
        populateCategoryDropdown("EXPENSE");
        setupDatePicker();

        // Check if editing
        editingTransaction = viewModel.getEditingTransaction().getValue();
        if (editingTransaction != null) {
            populateForEdit(editingTransaction);
        } else {
            binding.tvSheetTitle.setText(getString(R.string.add_transaction));
            binding.btnDelete.setVisibility(View.GONE);
            binding.etDate.setText(DateUtils.formatDate(selectedDate));
        }

        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnSave.setOnClickListener(v -> saveTransaction());
        binding.btnDelete.setOnClickListener(v -> deleteTransaction());
    }

    private void setupTypeToggle() {
        binding.toggleType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            selectedType = (checkedId == R.id.btn_income) ? "INCOME" : "EXPENSE";
            populateCategoryDropdown(selectedType);
        });
        // Default: expense selected
        binding.btnExpense.setChecked(true);
    }

    private void populateCategoryDropdown(String type) {
        List<Category> cats = "INCOME".equals(type)
                ? CategoryManager.getIncomeCategories()
                : CategoryManager.getExpenseCategories();

        String[] names = new String[cats.size()];
        for (int i = 0; i < cats.size(); i++) {
            names[i] = cats.get(i).icon + "  " + cats.get(i).name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                names);
        binding.actvCategory.setAdapter(adapter);
        if (names.length > 0) binding.actvCategory.setText(names[0], false);
    }

    private void setupDatePicker() {
        View.OnClickListener openPicker = v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText("Select date")
                    .setSelection(selectedDate)
                    .build();
            picker.addOnPositiveButtonClickListener(sel -> {
                selectedDate = sel;
                binding.etDate.setText(DateUtils.formatDate(sel));
            });
            picker.show(getChildFragmentManager(), "date_picker");
        };
        binding.etDate.setOnClickListener(openPicker);
        binding.tilDate.setEndIconOnClickListener(openPicker);
    }

    private void populateForEdit(Transaction t) {
        binding.tvSheetTitle.setText(getString(R.string.edit_transaction));
        binding.btnDelete.setVisibility(View.VISIBLE);

        // Type
        selectedType = t.type;
        if ("INCOME".equals(t.type)) {
            binding.btnIncome.setChecked(true);
            populateCategoryDropdown("INCOME");
        } else {
            binding.btnExpense.setChecked(true);
        }

        // Amount (strip prefix symbol)
        binding.etAmount.setText(String.valueOf(t.amount));

        // Category — match the emoji+name format
        Category cat = CategoryManager.findByName(t.category, t.type);
        binding.actvCategory.setText(cat.icon + "  " + cat.name, false);

        // Date
        selectedDate = t.date;
        binding.etDate.setText(DateUtils.formatDate(t.date));

        // Note
        if (t.note != null) binding.etNote.setText(t.note);
    }

    private void saveTransaction() {
        // Validate amount
        String amtStr = binding.etAmount.getText() != null
                ? binding.etAmount.getText().toString().trim() : "";
        if (amtStr.isEmpty()) {
            binding.tilAmount.setError(getString(R.string.error_amount_empty));
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException e) {
            binding.tilAmount.setError(getString(R.string.error_amount_invalid));
            return;
        }
        if (amount <= 0) {
            binding.tilAmount.setError(getString(R.string.error_amount_invalid));
            return;
        }
        binding.tilAmount.setError(null);

        // Parse category (strip emoji prefix)
        String rawCategory = binding.actvCategory.getText().toString().trim();
        String categoryName = rawCategory.contains("  ")
                ? rawCategory.substring(rawCategory.indexOf("  ") + 2)
                : rawCategory;
        Category cat = CategoryManager.findByName(categoryName, selectedType);

        String note = binding.etNote.getText() != null
                ? binding.etNote.getText().toString().trim() : "";

        if (editingTransaction != null) {
            // Update existing
            editingTransaction.amount        = amount;
            editingTransaction.type          = selectedType;
            editingTransaction.category      = cat.name;
            editingTransaction.categoryIcon  = cat.icon;
            editingTransaction.categoryColor = cat.color;
            editingTransaction.date          = selectedDate;
            editingTransaction.note          = note;
            viewModel.update(editingTransaction);
        } else {
            // Insert new
            Transaction t = new Transaction(amount, selectedType,
                    cat.name, cat.icon, cat.color, selectedDate, note);
            viewModel.insert(t);
        }

        viewModel.clearEditing();
        dismiss();
    }

    private void deleteTransaction() {
        if (editingTransaction != null) {
            viewModel.delete(editingTransaction);
            viewModel.clearEditing();
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
