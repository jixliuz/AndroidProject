package com.chauncy.account.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBDelegateImpl implements IDBDelegate {
    private DatabaseHelper helper;

    public DBDelegateImpl(Context context, String databaseName) {
        helper = DatabaseHelper.getInstance(context, databaseName);
    }

    @Override
    public boolean insertData(String tableName, String nullColumnHack, ContentValues values) {
        long row = 0;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            row = db.insert(tableName, null, values);
        } finally {
            if (db != null)
                db.close();
        }
        return row > 0;
    }

    @Override
    public boolean deleteData(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = null;
        int res = 0;
        try {
            db = helper.getWritableDatabase();
            res = db.delete(tableName, whereClause, whereArgs);
        } finally {
            if (db != null)
                db.close();
        }
        return res > 0;
    }

    @Override
    public boolean updateData(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = null;
        int res;
        try {
            db = helper.getWritableDatabase();
            res = db.update(tableName, values, whereClause, whereArgs);
        } finally {
            if (db != null)
                db.close();
        }
        return res > 0;
    }

    @Override
    public Map<String, String> getRowData(String tableName, String selection, String[] selectionArgs) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Map<String, String> map = new HashMap<>();
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
            int colsCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < colsCount; i++) {
                    String colsName = cursor.getColumnName(i);
                    String colsValue = cursor.getString(cursor.getColumnIndex(colsName));
                    map.put(colsName, colsValue);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return map;
    }

    @Override
    public List<Map<String, String>> getListData(String tableName, String selection, String[] selectionArgs) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Map<String, String>> list = new ArrayList<>();
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
            int colsCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < colsCount; i++) {
                    String colsName = cursor.getColumnName(i);
                    String colsValue = cursor.getString(cursor.getColumnIndex(colsName));
                    map.put(colsName, colsValue);
                }
                list.add(map);
            }
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return list;
    }
}
