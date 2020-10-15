package com.chauncy.account.model;

public class AccountInfo {
    private String accountType;
    private String accountId;
    private String type;
    private double asset;
    private double marketValue;
    private double todayProfit;
    private double todayProfitRatio;
    private double cash;
    private double maxPurchase;
    private double amountOwed;
    private double frozenCash;
    private double availableCash;
    private String riskLevel;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String cardType) {
        this.type = cardType;
    }

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
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

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getMaxPurchase() {
        return maxPurchase;
    }

    public void setMaxPurchase(double maxPurchase) {
        this.maxPurchase = maxPurchase;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public double getFrozenCash() {
        return frozenCash;
    }

    public void setFrozenCash(double frozenCash) {
        this.frozenCash = frozenCash;
    }

    public double getAvailableCash() {
        return availableCash;
    }

    public void setAvailableCash(double availableCash) {
        this.availableCash = availableCash;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }


}
