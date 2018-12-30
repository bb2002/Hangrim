package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.save
import kr.saintdev.hangrim.libs.func.share
import kr.saintdev.hangrim.modules.hgimage.HGImage
import kr.saintdev.hangrim.views.activities.ShuffleActivity
import java.io.File

class ShareFragment : Fragment() {
    private lateinit var v: View
    private lateinit var rootActivity: ShuffleActivity

    private lateinit var previewImage: ImageView
    private val savedStateArray = mutableMapOf<String, String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.activity_drawing_preview, container, false)
        this.rootActivity = activity as ShuffleActivity
        this.previewImage = this.v.findViewById(R.id.preview_image)

        // Set custom toolbar
        this.rootActivity.setToolbarBackbutton(true)

        // is resume fragment?
        val attachedImage = if(savedInstanceState == null) {
            // New start
            val shuffleFile = this.rootActivity.fragmentTemp["shuffle-file"] as File
            val drawingFile = this.rootActivity.fragmentTemp["draw-file"] as File

            if(shuffleFile.exists() && drawingFile.exists()) {
                val createdBitmap = HGImage.attachShuffleAndDrawing(shuffleFile, drawingFile)
                this.previewImage.setImageBitmap(createdBitmap)

                // save bitmap
                val filename = this.rootActivity.fragmentTemp["word-uuid"] as String
                val file = File(HGFunctions.getSaveFileLocation("$filename.jpg", this.rootActivity).absolutePath)
                createdBitmap.save(file)

                // create instance
                this.savedStateArray["word-uuid"] = filename
                file
            } else {
                // 파일이 존재하지 않을경우
                R.string.common_error.alert(R.string.shuffle_attach_failed, this.rootActivity)
                null
            }
        } else {
            // Resume
            val attachedImagePath = savedInstanceState.getString("attached-image")
            val wordUUID = savedInstanceState.getString("word-uuid")
            val wordEnglish = savedInstanceState.getString("word-english")
            val wordSymbol = savedInstanceState.getString("word-symbol")

            this.savedStateArray.clear()

            this.savedStateArray["word-uuid"] = wordUUID
            this.rootActivity.fragmentTemp["word-english"] = wordEnglish
            this.rootActivity.fragmentTemp["word-symbol"] = wordSymbol

            File(attachedImagePath)
        }

        // Show text info
        val wordEnglish = this.rootActivity.fragmentTemp["word-english"] as String
        val wordSymbol = this.rootActivity.fragmentTemp["word-symbol"] as String

        this.v.findViewById<EditText>(R.id.preview_title_editor).visibility = View.GONE     // Editor disable
        this.v.findViewById<EditText>(R.id.preview_comment_editor).visibility = View.GONE
        val titleView = this.v.findViewById<TextView>(R.id.preview_title_view)
        val contentView = this.v.findViewById<TextView>(R.id.preview_comment_view)
        titleView.visibility = View.VISIBLE
        contentView.visibility = View.VISIBLE
        titleView.text = wordEnglish
        contentView.text = wordSymbol

        if(attachedImage != null) {
            // save instance
            this.savedStateArray["word-english"] = wordEnglish
            this.savedStateArray["word-symbol"] = wordSymbol
            this.savedStateArray["attached-image"] = attachedImage.absolutePath

            // add listener
            this.v.findViewById<ImageButton>(R.id.preview_share_image).setOnClickListener {
                attachedImage.share(this.rootActivity)
            }
        }

        return this.v
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> rootActivity.finish()
        }

        return super.onContextItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("attached-image", savedStateArray["attached-image"])
        outState.putString("word-uuid", savedStateArray["word-uuid"])
        outState.putString("word-english", savedStateArray["word-english"])
        outState.putString("word-symbol", savedStateArray["word-symbol"])
        super.onSaveInstanceState(outState)
    }
}