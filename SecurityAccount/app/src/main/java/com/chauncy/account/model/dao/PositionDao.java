package com.chauncy.account.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.chauncy.account.model.bean.Position;

import java.util.ArrayList;
import java.util.List;

public class PositionDao {

    private String tbName = "position";
    private SQLiteDatabase db;

    public PositionDao(Context context) {
        this(context, "account.db");
    }

    public PositionDao(Context context, String dbName) {
        db =DatabaseManager.getInstance(context).getDatabase(dbName);
    }

    public long insert(Position data) {
        ContentValues values = new ContentValues();
        values.put("securityId", data.getStockId());
        values.put("accountId", data.getAccountId());
        values.put("name", data.getStockName());
        values.put("marketValue", data.getMarketValue());
        values.put("amount", data.getAmount());
        values.put("price", data.getPrice());
        values.put("cost", data.getCost());
        values.put("todayProfit", data.getTodayProfit());
        values.put("positionProfit", data.getPositionProfit());
        values.put("positionProfitRatio", data.getPositionProfitRatio());
        values.put("positionRatio", data.getPositionRatio());
        long row = db.insert(tbName, null, values);
        db.close();
        return row;

    }

    public void execSQL(String sql) {
        db.execSQL(sql);
        db.close();
    }

    public boolean deleteAccountPosition(String accountId) {
        int result = db.delete(tbName, "accountId=?", new String[]{accountId});
        db.close();
        return result > 0;
    }

    public List<Position> findAccountPosition(String accountID) {
        List<Position> result = new ArrayList<>();
        Cursor cursor = db.query(tbName, null, "accountId=?", new String[]{accountID}, null, null, null);
        while (cursor.moveToNext()) {
            Position element = new Position();
            element.setStockId(cursor.getString(0));
            element.setAccountId(cursor.getString(1));
            element.setStockName(cursor.getString(2));
            element.setMarketValue(cursor.getDouble(3));
            element.setAmount(cursor.getInt(4));
            element.setPrice(cursor.getDouble(5));
            element.setCost(cursor.getDouble(6));
            element.setTodayProfit(cursor.getDouble(7));
            element.setPositionProfit(cursor.getDouble(8));
            element.setPositionProfitRatio(cursor.getDouble(9));
            element.setPositionRatio(cursor.getDouble(10));
            result.add(element);
        }
        return result;
    }


    public List<Position> findAllPosition() {
        List<Position> result = new ArrayList<>();
        Cursor cursor = db.query(false, tbName, null, null, null, null,
                                 null, null, null);
        while (cursor.moveToNext()) {
            Position element = new Position();
            element.setStockId(cursor.getString(0));
            element.setAccountId(cursor.getString(1));
            element.setStockName(cursor.getString(2));
            element.setMarketValue(cursor.getDouble(3));
            element.setAmount(cursor.getInt(4));
            element.setPrice(cursor.getDouble(5));
            element.setCost(cursor.getDouble(6));
            element.setTodayProfit(cursor.getDouble(7));
            element.setPositionProfit(cursor.getDouble(8));
            element.setPositionProfitRatio(cursor.getDouble(9));
            element.setPositionRatio(cursor.getDouble(10));
            result.add(element);
        }
        cursor.close();
        db.close();
        return result;
    }
}
