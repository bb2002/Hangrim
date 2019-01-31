package kr.saintdev.hangrim.views.fragments.hangrim

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.*
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.hgimage.HGImage
import kr.saintdev.hangrim.modules.retrofit.MyExpressWord
import kr.saintdev.hangrim.views.activities.drawing.CreateCardActivity
import kr.saintdev.hangrim.views.activities.preview.MyExprViewActivity
import java.io.File

class ShareFragment : Fragment() {
    private lateinit var v: View
    private lateinit var rootActivity: CreateCardActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_drawing_load, container, false)
        this.rootActivity = activity as CreateCardActivity

        // Init anim
        val anime = AnimationUtils.loadAnimation(context, R.anim.rotate_anime)
        this.v.findViewById<ImageView>(R.id.splash_load).startAnimation(anime)

        // is resume fragment?
        val savePath = HGFunctions.getMyExpressPath(context!!)
        val task = AttachImageTask(savePath)
        task.execute(this.rootActivity.fragmentTemp["shuffle-file"] as File, this.rootActivity.fragmentTemp["draw-file"] as File)

        return v
    }

    inner class AttachImageTask(private val savePath: File) : AsyncTask<File, Void, File?>(){
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

                if(createdBitmap.save(savePath)) {
                    savePath
                } else {
                    null
                }
            }
        }

        override fun onPostExecute(result: File?) {
            super.onPostExecute(result)

            if(result == null) {
                // 저장 실패.
                R.string.common_error.alert(R.string.shuffle_attach_failed, rootActivity, DialogInterface.OnClickListener {
                    dialogInterface, _ ->
                    dialogInterface.dismiss()
                    rootActivity.finish()
                }) 
            } else {
                // DB 에 해당 표현 등록
                SQLManager.insertMyExpressWord(rootActivity,
                    MyExpressWord(result.path, result.name, "", ""))

                // 01.28 2019 겔러리에 저장 한다.
                context?.saveInGallery(result, File(HGFunctions.getGalleryPath(), "self_${getDateText()}.png"))
                // 01.28 2019 겔러리 저장 끝.


                // 저장 성공.
                val intent = Intent(rootActivity, MyExprViewActivity::class.java)
                intent.putExtra("image", result.absolutePath)
                intent.putExtra("isMoveCurrentPosition", true)
                startActivity(intent)
                rootActivity.finish()
            }
        }
    }
}