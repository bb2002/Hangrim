package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import kr.saintdev.hangrim.views.activities.ShuffleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShuffleFragment : Fragment() {
    private lateinit var v: View

    private lateinit var paintBoard: HGPaint
    private lateinit var rootActivity: ShuffleActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_shuffle_word, container, false)
        this.paintBoard = this.v.findViewById(R.id.shuffle_paint)
        this.rootActivity = activity as ShuffleActivity

        callRandomWord()        // 랜덤으로 단어를 가져온다.
        this.rootActivity.setToolbarBackbutton(true)

        val customBar = inflater.inflate(R.layout.hg_paint_toolbar, container, false)
        this.rootActivity.setCustomToolbar(customBar)

        return this.v
    }


    /**
     * 12.26 2018
     * 새로운 단어를 렌덤으로 받아 온다.
     */
    private fun callRandomWord() {
        val exceptID = HGFunctions.getShuffledWordsUUID(activity!!)       // 예외 목록

        val service = Retrofit.retrofit.create(HangrimService::class.java)
        val request = service.requestRandomWord("Any", exceptID.toString())
        request.enqueue(object : Callback<HangrimWord> {
            override fun onFailure(call: Call<HangrimWord>?, t: Throwable?) {
                R.string.common_error.str(activity!!)
                    .alert(R.string.shuffle_call_word_error.str(activity!!) + "\n" + t?.message,
                        activity!!)
            }

            override fun onResponse(call: Call<HangrimWord>, response: Response<HangrimWord>) {
                if(response.isSuccessful) {
                    val body = response.body()
                    paintBoard.setPlaceHolderText(body!!.word_korean)
                    paintBoard.setComment(body.word_english, body.word_symbol)
                } else {
                    onFailure(null, null)
                }
            }
        })
    }
}