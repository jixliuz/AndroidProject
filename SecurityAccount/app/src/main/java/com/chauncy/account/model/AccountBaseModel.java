package com.chauncy.account.model;


import com.chauncy.account.model.bean.SecurityAccount;

public class AccountBaseModel {
    private SecurityAccount mAccount;
    private int mWidgetType = -1;

    public AccountBaseModel(SecurityAccount account,int widgetType){
        mAccount=account;
        mWidgetType=widgetType;
    }

    public SecurityAccount getAccount(){
        return mAccount;
    }

    public void setAccount(SecurityAccount account){
        mAccount=account;
    }

    public int getWidgetType() {
        return mWidgetType;
    }

    public String getAccountType() {
        return mAccount.getAccountType();
    }


}
