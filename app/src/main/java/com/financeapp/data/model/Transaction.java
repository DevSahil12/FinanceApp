package com.financeapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "type")
    public String type; // "INCOME" or "EXPENSE"

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "category_icon")
    public String categoryIcon;

    @ColumnInfo(name = "category_color")
    public int categoryColor;

    @ColumnInfo(name = "date")
    public long date; // epoch millis

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public Transaction() {}

    public Transaction(double amount, String type, String category,
                       String categoryIcon, int categoryColor,
                       long date, String note) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
        this.date = date;
        this.note = note;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpense() {
        return "EXPENSE".equals(type);
    }

    public boolean isIncome() {
        return "INCOME".equals(type);
    }
}
