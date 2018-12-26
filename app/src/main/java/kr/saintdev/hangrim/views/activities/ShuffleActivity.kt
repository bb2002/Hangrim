package kr.saintdev.hangrim.views.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hangrim_shuffle.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Date 12.19 2018
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * Hangrim Shuffle Activity.
 */
class ShuffleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hangrim_shuffle)

        callRandomWord()
    }

    /**
     * 12.26 2018
     * 새로운 단어를 렌덤으로 받아 온다.
     */
    private fun callRandomWord() {
        val exceptID = HGFunctions.getShuffledWordsUUID(this)       // 예외 목록

        val service = Retrofit.retrofit.create(HangrimService::class.java)
        val request = service.requestRandomWord("Any", exceptID.toString())
        request.enqueue(object : Callback<HangrimWord>{
            override fun onFailure(call: Call<HangrimWord>?, t: Throwable?) {
                R.string.common_error.str(this@ShuffleActivity)
                    .alert(R.string.shuffle_call_word_error.str(this@ShuffleActivity) + "\n" + t?.message,
                        this@ShuffleActivity)
            }

            override fun onResponse(call: Call<HangrimWord>, response: Response<HangrimWord>) {
                if(response.isSuccessful) {
                    val body = response.body()
                    shuffle_paint.setPlaceHolderText(body!!.word_korean)
                    shuffle_paint.setComment(body!!.word_english, body!!.word_symbol)
                } else {
                    onFailure(null, null)
                }
            }
        })
    }
}