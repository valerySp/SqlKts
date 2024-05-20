package com.example.sqlnote.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.AccessControlContext

class MyDbHelper(context: Context): SQLiteOpenHelper(context,MyDB.DATABASE_NAME,null,
                        MyDB.DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyDB.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(MyDB.SQL_DELETE_TABLE)
        onCreate(db)

    }

}