package com.financeapp.data.model;

public class Category {
    public String name;
    public String icon;
    public int color;
    public String type; // "EXPENSE", "INCOME", "BOTH"

    public Category(String name, String icon, int color, String type) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.type = type;
    }
}
