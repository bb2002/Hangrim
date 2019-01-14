package kr.saintdev.hangrim.views.activities.preview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawing_preview.*
import kotlinx.android.synthetic.main.toolbar_default_close.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.str

class MySignaturePreview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_preview)

        preview_title_editor.visibility = View.GONE
        preview_share_image.visibility = View.GONE
        preview_comment_editor.visibility = View.GONE

        val signFile = HGFunctions.getSignaturePath(this)

        if(signFile.exists()) {
            preview_image.setImageBitmap(
                BitmapFactory.decodeFile(
                    signFile.absolutePath
                )
            )
        } else {
            Toast.makeText(this, R.string.sign_404.str(this), Toast.LENGTH_SHORT).show()
        }

        // Set next button click listener
        toolbar_default_close.setImageResource(R.drawable.ic_hgpaint_action_home)
        toolbar_default_close.setOnClickListener { finish() }
        preview_content.text = R.string.preview_message_sign.str(this)
    }
}