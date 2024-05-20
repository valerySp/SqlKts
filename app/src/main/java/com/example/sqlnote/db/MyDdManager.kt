package com.example.sqlnote.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDdManager (val context: Context) {

    val myDbHelper=MyDbHelper(context)
    var db:SQLiteDatabase?=null

    fun openDb(){
        db=myDbHelper.writableDatabase
    }

    fun insertToDb(title:String,content:String,uri:String,time:String){
        val values=ContentValues().apply {
            put(MyDB.COLUMN_NAME_TITLE,title)
            put(MyDB.COLUMN_NAME_CONTENT,content)
            put(MyDB.COLUMN_NAME_IMAGE_URI,uri)
            put(MyDB.COLUMN_NAME_TIME,time)
        }
        db?.insert(MyDB.TABLE_NAME,null,values)
    }

    fun updateItem(title:String,content:String,uri:String,id:Int,time:String){
        val select=BaseColumns._ID+"=$id"
        val values=ContentValues().apply {
            put(MyDB.COLUMN_NAME_TITLE,title)
            put(MyDB.COLUMN_NAME_CONTENT,content)
            put(MyDB.COLUMN_NAME_IMAGE_URI,uri)
            put(MyDB.COLUMN_NAME_TIME,time)
        }
        db?.update(MyDB.TABLE_NAME,values,select,null)
    }

    fun removeToDb(id:String){
        val select=BaseColumns._ID+"=$id"
        db?.delete(MyDB.TABLE_NAME,select,null)
    }


    @SuppressLint("Range")
    suspend fun readDbData(search:String):ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList=ArrayList<ListItem>()
        val select="${MyDB.COLUMN_NAME_TITLE} like ?"
        val cursor=db?.query(MyDB.TABLE_NAME,null,select, arrayOf("%$search%"),
            null,null,null)

        with (cursor){
            while (this?.moveToNext()!!){
                val dataID=cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val dataTitle=cursor?.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_TITLE))
                val dataCont=cursor?.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_CONTENT))
                val dataImgUri=cursor?.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_IMAGE_URI))
                val dataTime=cursor?.getString(cursor.getColumnIndex(MyDB.COLUMN_NAME_TIME))
                var item=ListItem()
                item.id= dataID!!
                item.title=dataTitle.toString()
                item.desc= dataCont.toString()
                item.uri=dataImgUri.toString()
                item.time=dataTime.toString()

                dataList.add(item)
            }
        }
        cursor?.close()
        return@withContext dataList
    }

    fun closeDb (){
        myDbHelper.close()
    }

}