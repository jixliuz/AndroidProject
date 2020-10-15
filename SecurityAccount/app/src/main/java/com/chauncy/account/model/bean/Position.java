package com.chauncy.account.model.bean;

public class Position {
    private String stockId;
    private String accountId;
    private String stockName;
    private double  marketValue;
    private int amount;
    private double price;
    private  double cost;
    private double todayProfit;
    private double positionProfit;
    private double positionProfitRatio;
    private double positionRatio;

    public Position() {
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(double todayProfit) {
        this.todayProfit = todayProfit;
    }

    public double getPositionProfit() {
        return positionProfit;
    }

    public void setPositionProfit(double positionProfit) {
        this.positionProfit = positionProfit;
    }

    public double getPositionProfitRatio() {
        return positionProfitRatio;
    }

    public void setPositionProfitRatio(double positionProfitRatio) {
        this.positionProfitRatio = positionProfitRatio;
    }

    public double getPositionRatio() {
        return positionRatio;
    }

    public void setPositionRatio(double positionRatio) {
        this.positionRatio = positionRatio;
    }


    @Override
    public String toString() {
        return "Position{" +
                "stockId='" + stockId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", stockName='" + stockName + '\'' +
                ", marketValue=" + marketValue +
                ", amount=" + amount +
                ", price=" + price +
                ", cost=" + cost +
                ", todayProfit=" + todayProfit +
                ", positionProfit=" + positionProfit +
                ", positionProfitRatio=" + positionProfitRatio +
                ", positionRatio=" + positionRatio +
                '}';
    }
}
