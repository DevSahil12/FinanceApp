package com.financeapp.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.financeapp.databinding.FragmentSettingsBinding;
import com.financeapp.utils.CurrencyFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences prefs;

    private static final String[] CURRENCIES     = {"INR", "USD", "EUR", "GBP"};
    private static final String[] CURRENCY_LABELS = {"INR (₹)", "USD ($)", "EUR (€)", "GBP (£)"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = requireContext().getSharedPreferences("finance_prefs",
                android.content.Context.MODE_PRIVATE);

        loadCurrentSettings();
        setupListeners();
    }

    private void loadCurrentSettings() {
        String currency = CurrencyFormatter.getCurrency(requireContext());
        for (int i = 0; i < CURRENCIES.length; i++) {
            if (CURRENCIES[i].equals(currency)) {
                binding.tvCurrencyValue.setText(CURRENCY_LABELS[i]);
                break;
            }
        }

        boolean darkMode = prefs.getBoolean("dark_mode", false);
        binding.switchDarkMode.setChecked(darkMode);

        boolean notifications = prefs.getBoolean("notifications", true);
        binding.switchNotifications.setChecked(notifications);
    }

    private void setupListeners() {
        binding.rowCurrency.setOnClickListener(v -> showCurrencyDialog());

        binding.switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("dark_mode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });

        binding.switchNotifications.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("notifications", checked).apply();
        });
    }

    private void showCurrencyDialog() {
        String current = CurrencyFormatter.getCurrency(requireContext());
        int selected = 0;
        for (int i = 0; i < CURRENCIES.length; i++) {
            if (CURRENCIES[i].equals(current)) { selected = i; break; }
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select Currency")
                .setSingleChoiceItems(CURRENCY_LABELS, selected, (d, which) -> {
                    CurrencyFormatter.setCurrency(requireContext(), CURRENCIES[which]);
                    binding.tvCurrencyValue.setText(CURRENCY_LABELS[which]);
                    d.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
