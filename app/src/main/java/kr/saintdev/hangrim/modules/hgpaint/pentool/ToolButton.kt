package kr.saintdev.hangrim.modules.hgpaint.pentool

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.dpToPixel
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.hgpaint.canvas.HGCanvasView

open abstract class ToolButton(context: Context, val penToolClick: OnPenToolClick) : ImageButton(context), View.OnClickListener {
    init {
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dpToPixel(context))
        param.setMargins(4,8,4,4)
        this.layoutParams = param
        this.setBackgroundColor(Color.RED)      // Debug
        this.scaleType = ScaleType.CENTER_INSIDE
        this.setOnClickListener(this)
    }
}