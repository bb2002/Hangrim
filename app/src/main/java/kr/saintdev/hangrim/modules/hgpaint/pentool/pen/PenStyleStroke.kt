package kr.saintdev.hangrim.modules.hgpaint.pentool.pen

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.View
import android.widget.Toast
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.ToolButton

class PenStyleStroke(context: Context, penToolClick: OnPenToolClick) : ToolButton(context, penToolClick) {
    override fun onClick(p0: View?) {
        val nowPen = penToolClick.hgCanvas.pen
        nowPen.style = Paint.Style.STROKE
        nowPen.pathEffect = null
    }
}
