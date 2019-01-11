package kr.saintdev.hangrim.views.fragments.hangrim

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.views.activities.drawing.CreateCardActivity
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import kr.saintdev.hgdrawing.hgdrawing.HGPaintView

class DrawPictureFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var rootActivity: CreateCardActivity
    private lateinit var paintBoard: HGPaintView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_drawing_pic, container, false)
        this.paintBoard = this.v.findViewById(R.id.canvas)
        this.rootActivity = activity as CreateCardActivity

        this.paintBoard.canvasStart()

        this.paintBoard.setBackwardListener(View.OnClickListener { if(!this.rootActivity.gotoBackward()) this.rootActivity.finish() }, null)
        this.paintBoard.setForwardListener(this, R.drawable.ic_hgpaint_action_download)
        return this.v
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

    override fun onStop() {
        super.onStop()
        this.paintBoard.canvasStop()
    }
}