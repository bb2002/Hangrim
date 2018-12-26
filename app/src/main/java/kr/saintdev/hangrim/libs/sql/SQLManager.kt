package kr.saintdev.hangrim.libs.sql

import android.content.Context
import kr.saintdev.hangrim.modules.retrofit.HangrimWord

object SQLManager {
    var dbPool: DBHelper? = null

    /**
     * 12.26 2018
     * DB 에서 따라쓰기를 마친 단어들을 가져온다.
     */
    fun getShuffledWord(context: Context) : ArrayList<HangrimWord> {
        if(dbPool == null) open(context)

        val read = dbPool!!.readableDatabase
        val csr = read.rawQuery(SQLQuery.READ_SHUFFLE_LOG, null)
        val dataArray = arrayListOf<HangrimWord>()

        while(csr.moveToNext()) {
            dataArray += HangrimWord(
                csr.getString(1), csr.getString(2), csr.getString(3), csr.getString(4), csr.getString(5))
        }

        return dataArray
    }

    /**
     * 12.26 2018
     * DB 를 연다.
     */
    fun open(context: Context) {
        SQLManager.dbPool = DBHelper(context, "Hangrim.db", null, 1)
    }
}

