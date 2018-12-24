package kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`

import android.content.Context
import android.graphics.Color
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenColor

object PenColors {
    fun addBlacks(context: Context, click: OnPenToolClick) =
        arrayOf(
            PenColor(context, click, Color.rgb(30,30,30)),
            PenColor(context, click, Color.rgb(61,61,61)),
            PenColor(context, click, Color.rgb(109,109,109)),
            PenColor(context, click, Color.rgb(255,255,255)))


    fun addColors(context: Context, click: OnPenToolClick) =
        arrayOf(
            PenColor(context, click, Color.rgb(174,84,83)),
            PenColor(context, click, Color.rgb(234,122,120)),
            PenColor(context, click, Color.rgb(199,165,137)),
            PenColor(context, click, Color.rgb(253,206,15)),
            PenColor(context, click, Color.rgb(193,208,115)),
            PenColor(context, click, Color.rgb(109,155,118)),
            PenColor(context, click, Color.rgb(181,219,232)),
            PenColor(context, click, Color.rgb(123,162,195)),
            PenColor(context, click, Color.rgb(146,144,228)),
            PenColor(context, click, Color.rgb(62,67,109))
        )
}