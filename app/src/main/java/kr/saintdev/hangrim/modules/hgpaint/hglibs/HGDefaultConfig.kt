package kr.saintdev.hangrim.modules.hgpaint.hglibs

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGFontLibrary.pxToDpi

object HGDefaultPaint {
    fun getPlaceHolderFontSize(context: Context) = pxToDpi(context, 80)

    fun getDefaultPaint() : Paint {
        val paint = Paint()
        paint.color = Color.rgb(44, 62, 80)
        paint.strokeWidth = HGPenSize.HG_NORMAL.size
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }

    fun getPlaceHolderPaint(c: Context) : Paint {
        val paint = getDefaultPaint()
        paint.color = Color.rgb(189, 195, 199)
        paint.textSize = getPlaceHolderFontSize(c)
        paint.typeface = Typeface.DEFAULT
        paint.isAntiAlias = true
        return paint
    }
}

enum class HGPenSize(val size: Float) {
    HG_THICK(11F),
    HG_NORMAL(7F),
    HG_THIN(4F)
}