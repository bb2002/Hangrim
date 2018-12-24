package kr.saintdev.hangrim.modules.hgpaint.pentool.pen

import android.content.Context
import android.graphics.Color
import android.view.View
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.ToolButton

class PenColor(context: Context, val pentool: OnPenToolClick, val color: Int) : ToolButton(context, pentool) {
    override fun onClick(v: View?) {
        pentool.hgCanvas.pen.color = color
    }
}