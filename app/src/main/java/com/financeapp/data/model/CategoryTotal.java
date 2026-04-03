package com.financeapp.data.model;

import androidx.room.ColumnInfo;

public class CategoryTotal {

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "category_icon")
    public String categoryIcon;

    @ColumnInfo(name = "category_color")
    public int categoryColor;

    @ColumnInfo(name = "total")
    public double total;

    @ColumnInfo(name = "transaction_count")
    public int transactionCount;

    public CategoryTotal() {}
}
