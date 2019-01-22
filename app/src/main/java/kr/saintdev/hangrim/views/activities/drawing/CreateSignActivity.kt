package kr.saintdev.hangrim.views.activities.drawing

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.modules.hgdrawing.HGPaintView
import kr.saintdev.hangrim.views.activities.preview.MySignaturePreview

class CreateSignActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var paintBoard: HGPaintView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sign)

        this.paintBoard = findViewById(R.id.sign_canvas)

        this.paintBoard.onCreate()
        this.paintBoard.setBackwardListener(View.OnClickListener { finish() }, null)
        this.paintBoard.setForwardListener(this, R.drawable.ic_hgpaint_action_download)

        Toast.makeText(this, R.string.ent_pag_dialog_9p, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        val signFile = HGFunctions.getSignaturePath(this)
        if(this.paintBoard.exportImage(signFile) == null) {
            Toast.makeText(this, R.string.sign_saveerr, Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this@CreateSignActivity, MySignaturePreview::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        this.paintBoard.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.paintBoard.onDestroy()
    }
}