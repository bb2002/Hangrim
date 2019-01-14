package kr.saintdev.hangrim.views.activities.preview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawing_preview.*
import kotlinx.android.synthetic.main.toolbar_default_close.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.share
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.retrofit.MyExpressWord
import java.io.File

/**
 * @Date 01.09 2019
 * MyExpress 를 작성하고 보여주는 Activity
 */
class MyExprViewActivity : AppCompatActivity(), TextWatcher {
    private lateinit var imagePath: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_preview)

        this.imagePath = File(intent.getStringExtra("image"))

        if(!imagePath.exists()) {
            // 파일이 없다.
            R.string.common_error.alert(R.string.drawing_preview_nofile, this)
        } else {
            // 파일을 표시 한다.
            preview_image.setImageBitmap(BitmapFactory.decodeFile(imagePath.absolutePath))

            // 해당 단어의 데이터 출력
            val uuid = this.imagePath.name
            val word = SQLManager.selectMyExpressWord(this, uuid)
            if(word != null) {
                preview_title_editor.text = SpannableStringBuilder(word.called)
                preview_comment_editor.text = SpannableStringBuilder(word.prono)
            }
        }

        // set listener
        preview_share_image.setOnClickListener {
            if(imagePath.exists()) imagePath.share(applicationContext)
            else Toast.makeText(applicationContext, R.string.common_file_err, Toast.LENGTH_SHORT).show()
        }

        // set text watcher
        preview_title_editor.addTextChangedListener(this)
        preview_comment_editor.addTextChangedListener(this)

        // Set next button click listener
        toolbar_default_close.setOnClickListener { finish() }

        preview_content.text = R.string.preview_message_shuffle.str(this)
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val uuid = this.imagePath.name

        // Text changed.
        SQLManager.updateMyExpressWord(this, uuid,
            MyExpressWord("", "",
                preview_title_editor.text.toString(),
                preview_comment_editor.text.toString())
        )
    }
}