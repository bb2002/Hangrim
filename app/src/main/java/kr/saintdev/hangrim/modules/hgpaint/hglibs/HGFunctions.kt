package kr.saintdev.hangrim.modules.hgpaint.hglibs

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.TypedValue

object HGFontLibrary {
    fun getTextSize(size: Int, text: String) : Rect {
        val p = Paint()
        val b = Rect()

        p.typeface = Typeface.DEFAULT
        p.textSize = size.toFloat()
        p.getTextBounds(text, 0, text.length, b)
        return b
    }

    fun pxToDpi(context: Context, px: Int) : Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            px.toFloat(),
            context.resources.displayMetrics
        )
    }

    /**
     * Get center Position
     */
    fun getCenterWidth(width: Int, sWidth: Int) = width / 2 - sWidth / 2
}