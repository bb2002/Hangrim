package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.modules.hgimage.HGImage
import kr.saintdev.hangrim.views.activities.ShuffleActivity
import java.io.File

class ShareFragment : Fragment() {
    private lateinit var v: View
    private lateinit var rootActivity: ShuffleActivity

    private lateinit var previewImage: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.activity_shuffle_share, container, false)
        this.rootActivity = activity as ShuffleActivity
        this.previewImage = this.v.findViewById(R.id.shuffle_preview_image)

        // Set custom toolbar
        val toolbar = inflater.inflate(R.layout.toolbar_shuffle_share, container, false)
        this.rootActivity.setCustomToolbar(toolbar)
        this.rootActivity.setToolbarBackbutton(false)

        // create attached bitmap
        val shuffleFile = this.rootActivity.fragmentTemp["shuffle-file"] as File
        val drawingFile = this.rootActivity.fragmentTemp["draw-file"] as File
        if(shuffleFile.exists() && drawingFile.exists()) {
            val createdBitmap = HGImage.attachShuffleAndDrawing(shuffleFile, drawingFile)
            this.previewImage.setImageBitmap(createdBitmap)
        } else {
            // 파일이 존재하지 않을경우
            R.string.common_error.alert(R.string.shuffle_attach_failed, this.rootActivity)
        }

        return this.v
    }
}