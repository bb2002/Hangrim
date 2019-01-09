package kr.saintdev.hangrim.libs.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, name: String, fact: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, fact, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQLQuery.SHUFFLE_LOG)            // 따라쓰기 기록 테이블
        db.execSQL(SQLQuery.MY_EXPR_LOG)            // 내 표현 기록
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}