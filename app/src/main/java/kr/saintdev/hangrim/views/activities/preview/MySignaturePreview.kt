package kr.saintdev.hangrim.views.activities.preview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawing_preview.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions

class MySignaturePreview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_preview)

        preview_title_editor.visibility = View.INVISIBLE
        preview_share_image.visibility = View.INVISIBLE
        preview_comment_editor.visibility = View.INVISIBLE

        val signFile = HGFunctions.getSignaturePath(this)

        if(signFile.exists()) {
            preview_image.setImageBitmap(
                BitmapFactory.decodeFile(
                    signFile.absolutePath
                )
            )
        } else {
            Toast.makeText(this, "사인이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}