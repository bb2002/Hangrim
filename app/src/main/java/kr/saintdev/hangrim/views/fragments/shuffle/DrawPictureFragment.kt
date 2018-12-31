package kr.saintdev.hangrim.views.fragments.shuffle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.hgpaint.toolbar.HGToolbarTool
import kr.saintdev.hangrim.modules.hgpaint.toolbar.OnToolClick
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity

class DrawPictureFragment : Fragment() {
    private lateinit var v: View
    private lateinit var rootActivity: ShuffleActivity

    private lateinit var paintBoard: HGPaint

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.v = inflater.inflate(R.layout.fragment_shuffle_dpicture, container, false)
        this.paintBoard = this.v.findViewById(R.id.shuffle_paint)
        this.rootActivity = activity as ShuffleActivity

        this.paintBoard.setHGPaintToolbar(this.rootActivity)
        this.paintBoard.setHGPaintToolListener(object : OnToolClick {
            override fun onClick(tool: HGToolbarTool, hgPaint: HGPaint) {
                when(tool) {
                    HGToolbarTool.FORWARD -> {
                        val drawFile = paintBoard.exportImage(HGFunctions.createTempFileName())
                        rootActivity.fragmentTemp["draw-file"] = drawFile
                        paintBoard.exit()           // Paint board off

                        rootActivity.gotoForward()
                    }
                    HGToolbarTool.BACKWARD -> {
                        rootActivity.gotoBackward()
                    }
                }
            }
        })
        this.rootActivity.setToolbarBackbutton(false)

        return this.v
    }
}