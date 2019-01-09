package kr.saintdev.hangrim.libs.sql

import android.content.Context
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.MyExpressWord

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
     * @Date 01.09 2019
     * DB 에서 내 표현을 가져온다.
     */
    fun getMyExpressWords(context: Context) : ArrayList<MyExpressWord> {
        if(dbPool == null) open(context)

        val read = dbPool!!.readableDatabase
        val csr = read.rawQuery(SQLQuery.READ_MY_EXPRESS_ALL_LOG, null)
        val dataArr = arrayListOf<MyExpressWord>()

        while(csr.moveToNext()) {
            dataArr +=
                    MyExpressWord(csr.getString(1), csr.getString(2), csr.getString(3), csr.getString(4))
        }

        return dataArr
    }

    /**
     * @Date 01.09 2019
     * DB 에 내 표현 한개 등록
     */
    fun insertMyExpressWord(context: Context, expr: MyExpressWord) {
        if(dbPool == null) open(context)
        val wr = dbPool!!.writableDatabase

        wr.execSQL(SQLQuery.WRITE_MY_EXPRESS_LOG, arrayOf(expr.imagePath, expr.uuid, expr.called, expr.prono))
    }

    /**
     * @Date 01.09 2019
     * DB 에 내 표현 한개 가져오기
     */
    fun selectMyExpressWord(context: Context, uuid: String) : MyExpressWord? {
        if(dbPool == null) open(context)

        val read = dbPool!!.readableDatabase
        val csr = read.rawQuery(SQLQuery.READ_MY_EXPRESS_LOG, arrayOf(uuid))
        return if(csr.moveToNext()) {
            val tmp = MyExpressWord(csr.getString(1), csr.getString(2), csr.getString(3), csr.getString(4))
            csr.close()
            tmp
        } else {
            null
        }
    }

    /**
     * @Date 01.09 2019
     * 내 표현을 수정한다.
     */
    fun updateMyExpressWord(context: Context, target: String, source: MyExpressWord) {
        if(dbPool == null) open(context)

        val wr = dbPool!!.writableDatabase
        wr.execSQL(SQLQuery.UPDATE_MY_EXPRESS_LOG, arrayOf(source.called, source.prono, target))
    }

    /**
     * 12.26 2018
     * DB 를 연다.
     */
    fun open(context: Context) {
        SQLManager.dbPool = DBHelper(context, "Hangrim.db", null, 1)
    }
}

