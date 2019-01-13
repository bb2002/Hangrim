package kr.saintdev.hangrim

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_main.*
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import kr.saintdev.hangrim.modules.retrofit.VerifyReamin
import kr.saintdev.hangrim.views.activities.drawing.CreateCardActivity
import kr.saintdev.hangrim.views.activities.drawing.CreateSignActivity
import kr.saintdev.hangrim.views.activities.list.MyCardActivity
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Portal Listener
        main_portal_mycards.setOnClickListener(portalButtonListener)
        main_portal_createcard.setOnClickListener(portalButtonListener)
        main_portal_createsign.setOnClickListener(portalButtonListener)
        main_portal_shuffle.setOnClickListener(portalButtonListener)
    }

    private var portalButtonListener = View.OnClickListener {
        val intent = when(it.id) {
            R.id.main_portal_mycards ->     // 내 카드목록으로 이동
                    Intent(this@MainActivity, MyCardActivity::class.java)
            R.id.main_portal_createcard ->  // 새 카드 만들기
                Intent(this@MainActivity, CreateCardActivity::class.java)
            R.id.main_portal_createsign ->  // 새 싸인 만들기
                Intent(this@MainActivity, CreateSignActivity::class.java)
            R.id.main_portal_shuffle -> {     // 단어 따라 쓰기 플레이
                startShufflePlay()
                null
            }
            else ->                         // 그 외 버튼
                Intent(this@MainActivity, ShuffleActivity::class.java)
        }

        if(intent != null)
            startActivity(intent)
    }

    private fun startShufflePlay() {
        val exceptID = HGFunctions.getShuffledWordsUUID(this)       // 예외 목록

        val service = Retrofit.getRetrofit().create(HangrimService::class.java)
        val request = service.isRemainWord(exceptID.toString())
        request.enqueue(object : Callback<VerifyReamin> {
            override fun onFailure(call: Call<VerifyReamin>, t: Throwable) {
                R.string.common_error.alert(R.string.main_portal_err, this@MainActivity)
            }

            override fun onResponse(call: Call<VerifyReamin>, response: Response<VerifyReamin>) {
                val body = response.body()

                if(body != null && body.is_remain)
                    startActivity(Intent(this@MainActivity, ShuffleActivity::class.java))
                else
                    R.string.main_portal_no_remain.alert(R.string.main_portal_no_remain_ctx, this@MainActivity)
            }
        })

    }
}
