package com.chauncy.account.model.bean;

public class AccountDetail {
    private double marketValue;
    private double cash;
    private double maxPurchase;
    private double amountOwed;
    private double frozenCash;
    private double cashAvailable;
    private String riskLevel;

    public AccountDetail() {
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
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

    public double getCashAvailable() {
        return cashAvailable;
    }

    public void setCashAvailable(double cashAvailable) {
        this.cashAvailable = cashAvailable;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
