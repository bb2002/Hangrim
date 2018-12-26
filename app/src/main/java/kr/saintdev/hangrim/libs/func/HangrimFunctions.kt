package kr.saintdev.hangrim.libs.func

import android.content.Context
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import org.json.JSONArray

object HGFunctions {
    fun getShuffeledWords(context: Context) : ArrayList<HangrimWord> {
        return SQLManager.getShuffledWord(context)
    }

    fun getShuffledWordsUUID(context: Context) : JSONArray {
        val arr = SQLManager.getShuffledWord(context)
        val dataArr = JSONArray()

        arr.forEach {
            dataArr.put(it.prop_uuid)
        }

        return dataArr
    }
}