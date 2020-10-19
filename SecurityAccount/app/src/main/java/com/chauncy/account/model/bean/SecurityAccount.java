package com.chauncy.account.model.bean;

public class SecurityAccount {
    private String id;
    private String accountType;
    private boolean isInfoShow = false;
    private int eventType = -1;

    public interface IAction {
        int ACCOUNT_CHANGE = 101;
        int INFO_STATE_CHANGE = 102;

    }

    public SecurityAccount(String accountType) {
        this.accountType = accountType;
        id="";
    }

    public SecurityAccount(String cardID, String accountType) {
        id = cardID;
        this.accountType = accountType;
    }

    public String getAccountID() {
        return id;
    }


    public void setAccountID(String id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType){
        this.accountType=accountType;
    }

    public boolean getInfoShowStatus() {
        return isInfoShow;
    }

    public void setInfoShowStatus(boolean isShow) {
        isInfoShow = isShow;
    }



    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "SecurityAccount{" +
                "id='" + id + '\'' +
                ", accountType='" + accountType + '\'' +
                ", isInfoShow=" + isInfoShow +
                ", eventType=" + eventType +
                '}';
    }
}
