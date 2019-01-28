package kr.saintdev.hangrim.views.fragments.shuffle

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.alirezabdn.wp7progress.WP10ProgressBar
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.save
import kr.saintdev.hangrim.libs.func.saveInGallery
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.hgimage.HGImage
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import kr.saintdev.hangrim.views.activities.preview.DrawingPreviewActivity
import java.io.File

class ShareFragment : Fragment() {
    private lateinit var v: View
    private lateinit var rootActivity: ShuffleActivity
    private lateinit var progressBar: WP10ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_drawing_load, container, false)
        this.rootActivity = activity as ShuffleActivity

        // Set custom toolbar
        this.rootActivity.setToolbarBackbutton(false)

        this.progressBar = this.v.findViewById(R.id.empty_progress)
        progressBar.showProgressBar()

        // is resume fragment?
        val filename = this.rootActivity.fragmentTemp["word-uuid"] as String
        val task = AttachImageTask("$filename.png")
        task.execute(this.rootActivity.fragmentTemp["shuffle-file"] as File, this.rootActivity.fragmentTemp["draw-file"] as File)

        return v
    }

    inner class AttachImageTask(private val fileName: String) : AsyncTask<File, Void, File?>(){
        override fun doInBackground(vararg files: File?): File? {
            val shuffleFile = files[0]
            val drawingFile = files[1]

            return if(shuffleFile == null || drawingFile == null || !shuffleFile.exists() || !drawingFile.exists()) {
                null
            } else {

                val createdBitmap = HGImage.attachShuffleAndDrawing(shuffleFile, drawingFile)
                // Sign 이 있는지 확인한다.
                val signPath = HGFunctions.getSignaturePath(rootActivity)
                if(signPath.exists()) {
                    // Sign 을 Attach 한다.
                    HGImage.attachSignInDrawing(createdBitmap, rootActivity)
                }
                val saveFile = File(HGFunctions.getSaveFileLocation(fileName, rootActivity).absolutePath)

                if(createdBitmap.save(saveFile)) {
                    saveFile
                } else {
                    null
                }
            }
        }

        override fun onPostExecute(result: File?) {
            super.onPostExecute(result)

            progressBar.hideProgressBar()
            if(result == null) {
                // 저장 실패.
                R.string.common_error.alert(R.string.shuffle_attach_failed, rootActivity, DialogInterface.OnClickListener {
                    dialogInterface, _ ->
                    dialogInterface.dismiss()
                    rootActivity.finish()
                }) 
            } else {
                // DB 에 기록을 남긴다.
                val uuid = rootActivity.fragmentTemp["word-uuid"] as String
                SQLManager.addShuffleCompleteWord(uuid, context!!)

                // 01.23 2019 겔러리에 저장 한다.
                context?.saveInGallery(result, File(HGFunctions.getGalleryPath(), "${rootActivity.fragmentTemp["word-english"]}.png"))
                // 01.23 2019 겔러리 저장 끝.

                // 저장 성공.
                val intent = Intent(rootActivity, DrawingPreviewActivity::class.java)
                intent.putExtra("image", result.absolutePath)
                intent.putExtra("word-english", rootActivity.fragmentTemp["word-english"] as String)
                intent.putExtra("word-symbol", rootActivity.fragmentTemp["word-symbol"] as String)
                intent.putExtra("word-category", rootActivity.fragmentTemp["word-category"] as String)
                intent.putExtra("word-uuid", uuid)
                intent.putExtra("ad", true)         // 01.24 2019 광고 노출을 실행 함.
                intent.putExtra("need-open",
                    rootActivity.fragmentTemp["need-open"] as Boolean)  // 01.24 2019 5p 이동을 지원함.
                startActivity(intent)

                rootActivity.finish()
            }
        }
    }
}