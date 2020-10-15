package com.chauncy.account.model.bean;

public class AssetsDetail {

    private double asset;

    private double todayProfit;

    private double todayProfitRatio;

    public AssetsDetail() {
    }

    public AssetsDetail(double asset, double todayProfit, double todayProfitRatio) {
        this.asset = asset;
        this.todayProfit = todayProfit;
        this.todayProfitRatio = todayProfitRatio;
    }

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public double getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(double todayProfit) {
        this.todayProfit = todayProfit;
    }

    public double getTodayProfitRatio() {
        return todayProfitRatio;
    }

    public void setTodayProfitRatio(double todayProfitRatio) {
        this.todayProfitRatio = todayProfitRatio;
    }
}
