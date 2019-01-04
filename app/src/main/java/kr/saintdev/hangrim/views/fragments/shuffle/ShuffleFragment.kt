package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.alirezabdn.wp7progress.WP10ProgressBar
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.hgpaint.toolbar.HGToolbarTool
import kr.saintdev.hangrim.modules.hgpaint.toolbar.OnToolClick
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShuffleFragment : Fragment(), OnToolClick {
    private lateinit var v: View

    private lateinit var paintBoard: HGPaint
    private lateinit var rootActivity: ShuffleActivity
    private lateinit var progressBar: WP10ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_shuffle_word, container, false)
        this.paintBoard = this.v.findViewById(R.id.shuffle_paint)
        this.rootActivity = activity as ShuffleActivity
        this.progressBar = this.v.findViewById(R.id.progress)

        callRandomWord()        // 랜덤으로 단어를 가져온다.
        this.paintBoard.setHGPaintToolbar(this.rootActivity)        // HGPaint Toolbar 를 적용 한다.
        this.paintBoard.setHGPaintToolListener(this)                // HGPaint Toolbar 의 Listener 를 적용 한다.
        this.rootActivity.setToolbarBackbutton(false)               // Backbutton 을 제거 한다.

        // progress run
        this.progressBar.showProgressBar()

        return this.v
    }


    /**
     * 12.26 2018
     * 새로운 단어를 렌덤으로 받아 온다.
     */
    private fun callRandomWord() {
        val exceptID = HGFunctions.getShuffledWordsUUID(activity!!)       // 예외 목록

        val service = Retrofit.getRetrofit().create(HangrimService::class.java)
        val request = service.requestRandomWord("Any", exceptID.toString())
        request.enqueue(object : Callback<HangrimWord> {
            override fun onFailure(call: Call<HangrimWord>?, t: Throwable?) {
                R.string.common_error.str(activity!!)
                    .alert(R.string.shuffle_call_word_error.str(activity!!) + "\n" + t?.message,
                        activity!!)
            }

            override fun onResponse(call: Call<HangrimWord>, response: Response<HangrimWord>) {
                val body = response.body()
                progressBar.hideProgressBar()

                if(response.isSuccessful && body != null) {
                    paintBoard.setPlaceHolderText(body.word_korean)           // Placeholder 을 그린다.
                    paintBoard.setComment(body.word_english, body.word_symbol)

                    rootActivity.fragmentTemp["word-english"] = body.word_english
                    rootActivity.fragmentTemp["word-symbol"] = body.word_symbol
                    rootActivity.fragmentTemp["word-uuid"] = body.prop_uuid
                } else {
                    onFailure(null, null)
                }
            }
        })
    }

    /**
     * @Date 12.28 2018
     * Backward / Forward 버튼 이벤트 처리
     */
    override fun onClick(tool: HGToolbarTool, hgPaint: HGPaint) {
        when(tool) {
            HGToolbarTool.FORWARD -> {
                // Bitmap 을 뽑아서 저장한다.
                val file = hgPaint.exportImage(HGFunctions.createTempFileName())
                this.rootActivity.fragmentTemp["shuffle-file"] = file

                this.rootActivity.gotoForward()
            }

            HGToolbarTool.BACKWARD -> {
                this.rootActivity.gotoBackward()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        this.paintBoard.exit()
    }
}