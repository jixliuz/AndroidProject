package com.chauncy.account.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.chauncy.account.model.bean.AccountOrder;

import java.util.ArrayList;
import java.util.List;

public class AccountOrderDao {
    private String tbName = "AccountOrder";
    private SQLiteDatabase db;

    public AccountOrderDao(Context context) {
        db = DatabaseManager.getInstance(context).getDatabase("account.db");
    }

    public void execSQL(String sql) {
        db.execSQL(sql);
        db.close();
    }

    public long insert(AccountOrder data) {
        ContentValues values = new ContentValues();
        values.put("id", data.getId());
        values.put("securityId", data.getStockId());
        values.put("securityName", data.getStockName());
        values.put("accountId", data.getAccountId());
        values.put("orderPrice", data.getOrderPrice());
        values.put("orderAmount", data.getOrderAmount());
        values.put("succAmount", data.getSuccAmount());
        values.put("orderStatus", data.getOrderStatus());
        values.put("submitStatus", data.getSubmitStatus());
        db.insert(tbName, null, values);
        return 0;
    }

    public boolean delete(String accountId) {
        int result = db.delete(tbName, "accountId=?", new String[]{accountId});
        db.close();
        return result > 0;
    }

    public List<AccountOrder> findAccountOrder(String accountId) {
        List<AccountOrder> result = new ArrayList<>();
        Cursor cursor = db.query(tbName, null, "accountId=?", new String[]{accountId}, null,
                                 null, null);
        while (cursor.moveToNext()) {
            AccountOrder element = new AccountOrder();
            element.setId(cursor.getInt(0));
            element.setStockName(cursor.getString(1));
            element.setStockId(cursor.getString(2));
            element.setAccountId(cursor.getString(3));
            element.setOrderPrice(cursor.getDouble(4));
            element.setOrderAmount(cursor.getInt(5));
            element.setSuccAmount(cursor.getInt(6));
            element.setOrderStatus(cursor.getString(7));
            element.setSubmitStatus(cursor.getString(8));
            result.add(element);
        }
        return result;
    }


    public List<AccountOrder> findAllOrder() {
        List<AccountOrder> result = new ArrayList<>();
        Cursor cursor = db.query(tbName, null, null, null, null,
                                 null, null, null);
        while (cursor.moveToNext()) {
            AccountOrder element = new AccountOrder();
            element.setId(cursor.getInt(0));
            element.setStockName(cursor.getString(1));
            element.setStockId(cursor.getString(2));
            element.setAccountId(cursor.getString(3));
            element.setOrderPrice(cursor.getDouble(4));
            element.setOrderAmount(cursor.getInt(5));
            element.setSuccAmount(cursor.getInt(6));
            element.setOrderStatus(cursor.getString(7));
            element.setSubmitStatus(cursor.getString(8));
            result.add(element);
        }
        return result;
    }

    public void close() {
        db.close();
    }
}
