package com.financeapp.data.model;

public class SummaryStats {
    public double totalBalance;
    public double totalIncome;
    public double totalExpense;
    public double monthIncome;
    public double monthExpense;

    public SummaryStats() {}

    public SummaryStats(double totalBalance, double totalIncome, double totalExpense,
                        double monthIncome, double monthExpense) {
        this.totalBalance = totalBalance;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.monthIncome = monthIncome;
        this.monthExpense = monthExpense;
    }

    public double getSavingsRate() {
        if (monthIncome == 0) return 0;
        return Math.max(0, (monthIncome - monthExpense) / monthIncome * 100);
    }
}
