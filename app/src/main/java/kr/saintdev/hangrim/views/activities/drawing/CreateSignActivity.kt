package kr.saintdev.hangrim.views.activities.drawing

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_sign.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.views.activities.preview.MySignaturePreview

class CreateSignActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sign)

        canvas.canvasStart()
        canvas.setBackwardListener(View.OnClickListener { finish() }, null)
        canvas.setForwardListener(this, null)
    }

    override fun onClick(p0: View?) {
        val signFile = HGFunctions.getSignaturePath(this)
        if(canvas.exportImage(signFile) == null) {
            Toast.makeText(this, R.string.sign_saveerr, Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this@CreateSignActivity, MySignaturePreview::class.java))
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        canvas.canvasStop()
    }
}