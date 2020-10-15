package com.chauncy.account.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseManager constructor(val context: Context) {

    private val databaseMap by lazy { mutableMapOf<String, SQLiteDatabase>() }

    companion object {
        private const val TAG = "DatabaseManager"
        private const val DATABASE_PATH = "/data/data/%s/database"
        private var instance: DatabaseManager?=null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): DatabaseManager? {
            if (instance == null)
                instance = DatabaseManager(context)
            return instance
        }
    }

    fun getDatabase(dbFile: String): SQLiteDatabase?{
        if (databaseMap[dbFile] != null) {
            Log.i(TAG, "Return a database copy of $dbFile")
            return databaseMap[dbFile]
        }
        if (context == null)
            return null
        val path = getDatabaseFilePath()
        val fileName = getDatabaseFile(dbFile)
        var file = File(fileName)

        val sharedPreferences = context.getSharedPreferences(DatabaseManager::class.java.toString(), 0)
        val flag = sharedPreferences.getBoolean(dbFile, false)//true表示文件已存在
        if (!flag || !file.exists()) {
            file = File(path)
            if (!file.exists() && !file.mkdirs()) {
                Log.i(TAG, "Create $path fail!")
                return null
            }
            if (!copyFileToAndroidSystem(dbFile, fileName)) {
                Log.i(TAG, "Copy $dbFile to $fileName fail!")
                return null
            }

            sharedPreferences.edit().putBoolean(dbFile, true).commit()
        }
        val db = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS)
        if (db != null) {
            databaseMap[dbFile] = db
        }
        return db
    }

    private fun copyFileToAndroidSystem(srcFile: String, destFile: String): Boolean {
        Log.i(TAG, "Copy $srcFile to $destFile")
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = context.assets.open(srcFile)
            outputStream = FileOutputStream(destFile)
            val buffer = ByteArray(1024)
            var length=inputStream.read(buffer)
            while (length>0) {
                outputStream?.write(buffer, 0, length)
                length=inputStream.read(buffer)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (ee: Exception) {
                ee.printStackTrace()
            }
            return false
        }

        return true
    }

    private fun getDatabaseFile(dbFile: String): String {
        return getDatabaseFilePath() + "/" + dbFile
    }

    private fun getDatabaseFilePath(): String {
        return String.format(DATABASE_PATH, context.applicationInfo.packageName)
    }

    fun closeDatabase(dbFile: String): Boolean {
        if (databaseMap[dbFile] != null) {
            val db = databaseMap[dbFile]
            db?.close()
            databaseMap.remove(dbFile)
            return true
        }
        return false
    }

}