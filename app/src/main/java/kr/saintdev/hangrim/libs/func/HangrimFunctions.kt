package kr.saintdev.hangrim.libs.func

import android.content.Context
import android.os.Environment
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import org.json.JSONArray
import java.io.File

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

    // 임시파일의 저장 경로.
    fun getTempFileLocation(filename: String, context: Context) =
        File(context.cacheDir, filename)

    // 실제 파일의 저장 경로
    fun getSaveFileLocation(filename: String, context: Context) =
            File(context.filesDir, filename)

    fun createTempFileName() =
            System.currentTimeMillis().toString() + ".png"
}