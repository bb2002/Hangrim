package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.modules.hgdrawing.HGPaintView
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity

class DrawPictureFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var rootActivity: ShuffleActivity
    private lateinit var paintBoard: HGPaintView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_drawing_pic, container, false)
        this.paintBoard = this.v.findViewById(R.id.canvas)
        this.rootActivity = activity as ShuffleActivity
        this.paintBoard.onCreate()
        this.paintBoard.setBackwardListener(View.OnClickListener { if(!this.rootActivity.gotoBackward()) this.rootActivity.finish() }, null)
        this.paintBoard.setForwardListener(this, R.drawable.ic_hgpaint_action_download)

        this.rootActivity.setToolbarBackbutton(false)
        Toast.makeText(context, R.string.ent_pag_dialog_3p, Toast.LENGTH_SHORT).show()
        return this.v
    }

    override fun onResume() {
        super.onResume()
        this.paintBoard.onResume()
    }

    /**
     * @Date 01.08 2019
     * Backward / Forward 버튼 이벤트 처리
     */
    override fun onClick(view: View) {
        // Bitmap 을 뽑아서 저장한다.
        val file = paintBoard.exportImage(HGFunctions.getTempFileLocation(context!!))
        this.rootActivity.fragmentTemp["draw-file"] = file
        this.rootActivity.gotoForward()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.paintBoard.onDestroy()
    }
}