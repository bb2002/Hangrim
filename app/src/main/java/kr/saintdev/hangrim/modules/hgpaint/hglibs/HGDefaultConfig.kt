package kr.saintdev.hangrim.modules.hgpaint.hglibs

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface

object HGDefaultPaint {
    const val PLACEHOLDER_FONT_SIZE = 240F

    fun getDefaultPaint() : Paint {
        val paint = Paint()
        paint.color = Color.rgb(44, 62, 80)
        paint.strokeWidth = HGPenSize.HG_NORMAL.size
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        return paint
    }

    fun getPlaceHolderPaint() : Paint {
        val paint = getDefaultPaint()
        paint.color = Color.rgb(189, 195, 199)
        paint.textSize = PLACEHOLDER_FONT_SIZE
        paint.typeface = Typeface.DEFAULT
        paint.isAntiAlias = true
        return paint
    }
}

enum class HGPenSize(val size: Float) {
    HG_THICK(13F),
    HG_NORMAL(9F),
    HG_THIN(6F)
}