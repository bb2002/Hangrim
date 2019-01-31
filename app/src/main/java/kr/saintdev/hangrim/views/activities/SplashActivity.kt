package kr.saintdev.hangrim.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kr.saintdev.hangrim.MainActivity
import kr.saintdev.hangrim.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler {
            startActivity(
                Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            true
        }
        handler.sendEmptyMessageDelayed(0, 2000)
    }
}