package kr.saintdev.hangrim.modules.hgpaint.pentool.pen

import android.content.Context
import android.view.View
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.ToolButton

class PenThickness(context: Context, penToolClick: OnPenToolClick, val thickness: Float) : ToolButton(context, penToolClick) {
    override fun onClick(v: View?) {
        penToolClick.hgCanvas.pen.strokeWidth = thickness
    }
}