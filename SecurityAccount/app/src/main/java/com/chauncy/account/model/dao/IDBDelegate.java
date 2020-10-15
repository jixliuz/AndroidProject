package com.chauncy.account.model.dao;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

public interface IDBDelegate {
    boolean insertData(String tableName, String column, ContentValues values);

    boolean deleteData(String tableName,String whereClause,String[] whereArgs);

    boolean updateData(String tableName,ContentValues values,String whereClause,String[] whereArgs);

    Map<String,String> getRowData(String tableName,String selection,String[] selectionArgs);

    List<Map<String,String>> getListData(String tableName, String selection, String[] selectionArgs);
}
