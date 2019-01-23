package kr.saintdev.hangrim.views.activities.preview

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.fsn.cauly.CaulyCloseAd
import com.fsn.cauly.CaulyCloseAdListener
import kotlinx.android.synthetic.main.activity_drawing_preview.*
import kotlinx.android.synthetic.main.toolbar_default_close.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.ActivityFunctions
import kr.saintdev.hangrim.libs.func.Ads
import kr.saintdev.hangrim.libs.func.share
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.views.activities.list.MyCardActivity
import java.io.File

/**
 * @Date 12.31 2018
 * Shuffle 후 데이터를 확인하는 액티비티 입니다.
 * intent["image"] = image path
 * intent["word-english"] = word.en
 * intent["word-symbol"] = word.symbol
 */

class DrawingPreviewActivity : AppCompatActivity() {
    private lateinit var imagePath: String
    private lateinit var wordEnglish: String
    private lateinit var wordSymbol: String
    private var wordCategory: String? = null
    private var wordUUID: String? = null
    private var caulyAD: CaulyCloseAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_preview)

        // Run preview mode
        ActivityFunctions.enablePreviewMode(
            preview_comment_editor, preview_title_editor, preview_title_view, preview_comment_view)

        // Get param
        this.imagePath = intent.getStringExtra("image")
        this.wordEnglish = intent.getStringExtra("word-english")
        this.wordSymbol = intent.getStringExtra("word-symbol")
        this.wordCategory = intent.getStringExtra("word-category") ?: null
        this.wordUUID = intent.getStringExtra("word-uuid") ?: null

        this.caulyAD = Ads.createAds(this)

        // set listener
        preview_share_image.setOnClickListener {
            val file = File(imagePath)
            if(file.exists()) {
                // File 이 존재할경우 공유 실행
                file.share(applicationContext)
            } else {
                Toast.makeText(applicationContext, R.string.common_file_err, Toast.LENGTH_SHORT).show()
            }
        }

        // Set next button click listener
        toolbar_default_close.setOnClickListener {
            if(this.wordCategory != null && this.wordUUID != null) {
                val intent = Intent(applicationContext, MyCardActivity::class.java)
                intent.putExtra("category", this.wordCategory)
                intent.putExtra("uuid", this.wordUUID)
                startActivity(intent)
            }

            finish()
        }

        preview_content.text = R.string.preview_message_shuffle.str(this)
    }

    override fun onResume() {
        super.onResume()
        this.caulyAD?.resume(this)
    }


    override fun onStart() {
        super.onStart()

        // Show data
        preview_image.setImageBitmap(BitmapFactory.decodeFile(imagePath))
        preview_title_view.text = this.wordEnglish
        preview_comment_view.text = this.wordSymbol
    }
}