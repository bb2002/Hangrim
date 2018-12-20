package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint.PLACEHOLDER_FONT_SIZE
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGFontLibrary

class HGCanvasView(context: Context) : View(context) {
    private val points = arrayListOf<HGPoint>()     // Draw Points
    private var pen: Paint = HGDefaultPaint.getDefaultPaint()         // Now Pen

    // HGCanvasView Properties
    var placeholderText: String = ""    // PlaceHolder Text

    override fun onDraw(canvas: Canvas?) {
        if(canvas != null) {
            // Draw Placeholder Text
            val psHolderTextSize = HGFontLibrary.getTextSize(PLACEHOLDER_FONT_SIZE.toInt(), placeholderText)
            canvas.drawText(placeholderText,
                    HGFontLibrary.getCenterWidth(width, psHolderTextSize.width()).toFloat(),
                    (psHolderTextSize.height() + HGFontLibrary.getCenterWidth(height, psHolderTextSize.height())).toFloat(),
                    HGDefaultPaint.getPlaceHolderPaint())

            // Draw User image
            for(i in 1 until points.size) {
                val p = points[i]
                if(p.isDraw) canvas.drawLine(points[i - 1].x, points[i - 1].y, p.x, p.y, p.paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y

            when(event.action) {
                MotionEvent.ACTION_DOWN ->
                    points.add(HGPoint(x, y, pen, false))
                MotionEvent.ACTION_MOVE ->
                    points.add(HGPoint(x, y, pen, true))
            }

            invalidate()
            return true
    }
}