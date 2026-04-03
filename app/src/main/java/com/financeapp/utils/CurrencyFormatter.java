package com.financeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final String PREF_CURRENCY = "pref_currency";
    private static final String DEFAULT_CURRENCY = "INR";

    public static String format(Context context, double amount) {
        String currency = getCurrency(context);
        return format(amount, currency);
    }

    public static String format(double amount, String currency) {
        switch (currency) {
            case "INR":
                return "₹" + formatNumber(amount, new Locale("en", "IN"));
            case "USD":
                return "$" + formatNumber(amount, Locale.US);
            case "EUR":
                return "€" + formatNumber(amount, Locale.GERMANY);
            case "GBP":
                return "£" + formatNumber(amount, Locale.UK);
            default:
                return currency + " " + formatNumber(amount, Locale.getDefault());
        }
    }

    private static String formatNumber(double amount, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return nf.format(amount);
    }

    public static String getCurrency(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("finance_prefs", Context.MODE_PRIVATE);
        return prefs.getString(PREF_CURRENCY, DEFAULT_CURRENCY);
    }

    public static void setCurrency(Context context, String currency) {
        context.getSharedPreferences("finance_prefs", Context.MODE_PRIVATE)
               .edit().putString(PREF_CURRENCY, currency).apply();
    }

    public static String formatShort(Context context, double amount) {
        String symbol = getCurrencySymbol(context);
        if (amount >= 100000) {
            return symbol + String.format(Locale.getDefault(), "%.1fL", amount / 100000);
        } else if (amount >= 1000) {
            return symbol + String.format(Locale.getDefault(), "%.1fK", amount / 1000);
        }
        return format(context, amount);
    }

    private static String getCurrencySymbol(Context context) {
        switch (getCurrency(context)) {
            case "INR": return "₹";
            case "USD": return "$";
            case "EUR": return "€";
            case "GBP": return "£";
            default: return "";
        }
    }
}
