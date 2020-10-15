package com.chauncy.account.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context,String name) {
        if(instance==null){
            instance=new DatabaseHelper(context,name);
        }
        return instance;
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name) {
        super(context,name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String position_table_sql="create table 'position'(securityId text,accountId text," +
                "securityName text, marketValue double,amount integer,price double,cost double," +
                "todayProfit double,positionProfit double,positionProfitRatio double,positionRatio double,primary key(securityId,accountId))";
        String account_order_sql="create table 'accountOrder'(id integer primary key,securityName text,securityId text,accountId text,orderPrice double,orderAmount integer,succAmount integer,orderStatus text,submitStatus text)";
        db.execSQL(position_table_sql);
        db.execSQL(account_order_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
