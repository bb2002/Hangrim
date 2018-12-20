package kr.saintdev.hangrim.modules.hgpaint.hglibs

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface

object HGFontLibrary {
    fun getTextSize(size: Int, text: String) : Rect {
        val p = Paint()
        val b = Rect()

        p.typeface = Typeface.DEFAULT
        p.textSize = size.toFloat()
        p.getTextBounds(text, 0, text.length, b)
        return b
    }

    /**
     * Get center Position
     */
    fun getCenterWidth(width: Int, sWidth: Int) = width / 2 - sWidth / 2
}