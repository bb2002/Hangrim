package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.alirezabdn.wp7progress.WP10ProgressBar
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.modules.hgdrawing.HGPaintView
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShuffleFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View

    private lateinit var rootActivity: ShuffleActivity
    private lateinit var progressBar: WP10ProgressBar
    private lateinit var paintBoard: HGPaintView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_drawing_word, container, false)
        this.paintBoard = this.v.findViewById(R.id.canvas)
        this.rootActivity = activity as ShuffleActivity
        this.progressBar = this.v.findViewById(R.id.progress)
        this.paintBoard.onCreate()

        this.paintBoard.setBackwardListener(View.OnClickListener {
            if(!this.rootActivity.gotoBackward()) this.rootActivity.finish()
        }, null)
        this.paintBoard.setForwardListener(this, null)

        return this.v
    }

    /**
     * 12.26 2018
     * 새로운 단어를 렌덤으로 받아 온다.
     */
    private fun callRandomWord() {
        this.progressBar.showProgressBar()
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
                    paintBoard.setCommentMessage(body.word_english, body.word_symbol)

                    rootActivity.fragmentTemp["word-english"] = body.word_english
                    rootActivity.fragmentTemp["word-symbol"] = body.word_symbol
                    rootActivity.fragmentTemp["word-uuid"] = body.prop_uuid
                    rootActivity.fragmentTemp["word-category"] = body.prop_category
                    rootActivity.fragmentTemp["word-korean"] = body.word_korean

                    // Toast 를 열어 작성을 시작을 요청 한다.
                    Toast.makeText(context, R.string.ent_pag_dialog_2p, Toast.LENGTH_LONG).show()
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
    override fun onClick(view: View) {
        // Bitmap 을 뽑아서 저장한다.
        val file = paintBoard.exportImage(HGFunctions.getTempFileLocation(context!!))
        this.rootActivity.fragmentTemp["shuffle-file"] = file
        this.rootActivity.gotoForward()
    }

    override fun onResume() {
        super.onResume()

        if(rootActivity.fragmentTemp["word-preload"] != null) {
            paintBoard.setPlaceHolderText(rootActivity.fragmentTemp["word-korean"] as String)           // Placeholder 을 그린다.
            paintBoard.setCommentMessage(
                rootActivity.fragmentTemp["word-english"] as String,
                rootActivity.fragmentTemp["word-symbol"] as String)
        } else if(rootActivity.fragmentTemp["loaded"] == null) {
            callRandomWord()        // 랜덤으로 단어를 가져온다.
            rootActivity.fragmentTemp["word-preload"] = true
        }

        this.paintBoard.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.paintBoard.onDestroy()
    }
}