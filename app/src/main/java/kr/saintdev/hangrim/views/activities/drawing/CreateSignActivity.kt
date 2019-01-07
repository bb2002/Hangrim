package kr.saintdev.hangrim.views.activities.drawing

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_sign.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.alert

class CreateSignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sign)

//        sign_paint.setHGPaintToolbar(this)
//        sign_paint.setHGPaintToolListener(object : OnToolClick {
//            override fun onClick(tool: HGToolbarTool, hgPaint: HGPaint) {
//                sign_paint.exit()
//
//                when(tool) {
//                    HGToolbarTool.BACKWARD -> {
//                        finish()
//                    }
//                    HGToolbarTool.FORWARD -> {
//                        val tmpFile = sign_paint.exportImage("signature.png")
//                        if(tmpFile == null) {
//                            R.string.common_error.alert(R.string.sign_saveerr, this@CreateSignActivity)
//                        } else {
////                            if(tmpFile.renameTo(HGFunctions.getSignaturePath(this@CreateSignActivity))) {
////                                // 사인을 저장하였습니다.
////                                startActivity(Intent(this@CreateSignActivity, MySignaturePreview::class.java))
////                            } else {
////                                R.string.common_error.alert(R.string.sign_saveerr, this@CreateSignActivity)
////                            }
//
//                            tmpFile.renameTo(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
//                        }
//                    }
//                }
//            }
//        })
    }
}