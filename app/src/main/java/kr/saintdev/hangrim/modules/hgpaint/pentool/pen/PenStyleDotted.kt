package kr.saintdev.hangrim.modules.hgpaint.pentool.pen

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.ToolButton
import android.graphics.DashPathEffect
import android.view.View
import android.support.v4.view.ViewCompat.setAlpha
import kr.saintdev.hangrim.modules.hgpaint.canvas.DrawMode


class PenStyleDotted(context: Context, penToolClick: OnPenToolClick) : ToolButton(context, penToolClick) {
    override fun onClick(p0: View?) {
//        penToolClick.hgCanvas.penMode = DrawMode.CRAYON
    }
}