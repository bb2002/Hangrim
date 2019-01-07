package kr.saintdev.hangrim.modules.hgdrawing.property

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGConvert

data class HGCanvasProperty(
    var isAlpha: Boolean = false,        // 배경에 투명을 사용하는가.
    var useColorTool: Boolean = true,     // Color selector 를 사용 하는가?
    var canvasWidth: Int = 0,       // Canvas width
    var canvasHeight: Int = 0,       // Canvas height
    var placeholderText: String = ""    // Placeholder
)

object DefaultPaint {
    fun getPlaceHolderTextSize(context: Context) =
            HGConvert.pxToDpi(100, context)

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
        paint.typeface = Typeface.DEFAULT
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        return paint
    }
}

enum class PenSize(val size: Float) {
    HG_THICK(11F),
    HG_NORMAL(7F),
    HG_THIN(4F)
}