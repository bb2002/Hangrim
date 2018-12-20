package kr.saintdev.hangrim.modules.hgpaint.hglibs

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface

object HGDefaultPaint {
    const val PLACEHOLDER_FONT_SIZE = 300F

    fun getDefaultPaint() : Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = HGPenSize.HG_NORMAL.size
        paint.style = Paint.Style.FILL
        return paint
    }

    fun getPlaceHolderPaint() : Paint {
        val paint = getDefaultPaint()
        paint.color = Color.GRAY
        paint.textSize = PLACEHOLDER_FONT_SIZE
        paint.typeface = Typeface.DEFAULT
        return paint
    }

    fun getDivideLinePaint() : Paint {
        val paint = Paint()
        paint.strokeWidth = 20F
        paint.color = Color.GRAY
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(2F, 4F), 50F)
        return paint
    }
}

enum class HGPenSize(val size: Float) {
    HG_THICK(13F),
    HG_NORMAL(9F),
    HG_THIN(6F)
}