package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint.getPlaceHolderFontSize
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGFontLibrary

class HGCanvasView(context: Context) : View(context) {
    private val points = arrayListOf<HGPoint>()     // Draw Points
    val pen: Paint = HGDefaultPaint.getDefaultPaint()         // Now Pen

    // HGCanvasView Properties
    var placeholderText: String = ""    // PlaceHolder Text
    private val path = Path()                   // Path instance

    override fun onDraw(canvas: Canvas?) {
        if(canvas != null) {
            // Draw Placeholder Text
            val psHolderTextSize = HGFontLibrary.getTextSize(getPlaceHolderFontSize(context).toInt(), placeholderText)
            canvas.drawText(placeholderText,
                    HGFontLibrary.getCenterWidth(width, psHolderTextSize.width()).toFloat(),
                    (psHolderTextSize.height() + HGFontLibrary.getCenterWidth(height, psHolderTextSize.height())).toFloat(),
                    HGDefaultPaint.getPlaceHolderPaint(context))

            // Draw User image
            for(i in 1 until points.size) {
                val p = points[i]
                path.reset()
                path.moveTo(points[i - 1].x, points[i - 1].y)
                path.lineTo(p.x, p.y)

                if(p.isDraw) canvas.drawPath(path, p.paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y

            when(event.action) {
                MotionEvent.ACTION_DOWN ->
                    points.add(HGPoint(x, y, Paint(pen), false))
                MotionEvent.ACTION_MOVE ->
                    points.add(HGPoint(x, y, Paint(pen), true))
            }

            invalidate()
            return true
    }

    fun resetCanvas() {
        this.points.clear()
        invalidate()
    }
}