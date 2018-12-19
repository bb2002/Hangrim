package kr.saintdev.hangrim

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kr.saintdev.hangrim.views.activities.CreateCardActivity
import kr.saintdev.hangrim.views.activities.CreateSignActivity
import kr.saintdev.hangrim.views.activities.MyCardActivity
import kr.saintdev.hangrim.views.activities.ShuffleActivity

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
            R.id.main_portal_shuffle ->     // 단어 따라 쓰기 플레이
                Intent(this@MainActivity, ShuffleActivity::class.java)
            else ->                         // 그 외 버튼
                Intent(this@MainActivity, ShuffleActivity::class.java)
        }

        startActivity(intent)
    }
}
