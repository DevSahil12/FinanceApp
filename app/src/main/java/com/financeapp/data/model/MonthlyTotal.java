package com.financeapp.data.model;

import androidx.room.ColumnInfo;

public class MonthlyTotal {

    @ColumnInfo(name = "month_label")
    public String monthLabel; // e.g. "Jan", "Feb"

    @ColumnInfo(name = "month_key")
    public String monthKey; // e.g. "2024-01"

    @ColumnInfo(name = "income")
    public double income;

    @ColumnInfo(name = "expense")
    public double expense;

    public MonthlyTotal() {}

    public double getNet() {
        return income - expense;
    }
}
