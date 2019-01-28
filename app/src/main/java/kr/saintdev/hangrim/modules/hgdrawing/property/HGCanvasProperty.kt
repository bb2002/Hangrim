package kr.saintdev.hangrim.modules.hgdrawing.property

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGConvert

object DefaultPaint {
    fun getPlaceHolderTextSize(context: Context) =
            HGConvert.pxToDpi(120, context)

    fun getDefaultPaint() : Paint {
        val paint = Paint()
        paint.color = Color.rgb(30,30,30)
        paint.strokeWidth = PenSize.HG_NORMAL.size
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        return paint
    }

    fun getPlaceHolderPaint(size: Float) : Paint {
        val paint = getDefaultPaint()
        paint.color = Color.rgb(189, 195, 199)
        paint.textSize = size
        paint.typeface = Typeface.MONOSPACE
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        return paint
    }
}

object EtcProperty {
    const val UNDO_SIZE = 8
    const val REDO_SIZE = 8
    const val VIBRATION_SIZE = 1000

    const val PEN_BUTTON_VIBRATION_SIZE = 1000
}

enum class PenSize(val size: Float) {
    HG_THICK(20F),
    HG_NORMAL(15F),
    HG_THIN(7F)
}