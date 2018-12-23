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

    // HGCanvasView Properties
    var placeholderText: String = ""        // PlaceHolder Text
    private val path = Path()               // Path instance
    var penMode: DrawMode = DrawMode.PEN    // 기본 드로우 모드
    val pen: Paint = HGDefaultPaint.getDefaultPaint()         // Now Pen

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
                if(!p.isDraw) continue

                when(p.drawMode) {
                    DrawMode.PEN -> {
                            path.moveTo(points[i - 1].x, points[i - 1].y)
                            path.lineTo(p.x, p.y)
                            canvas.drawPath(path, p.paint)
                            path.reset()
                    }

                    DrawMode.CRAYON -> {
//                        for(l in p.crayonPoints) {
//                            canvas.drawPoint(l.x, l.y, p.paint)
//                        }
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y

            when(event.action) {
                MotionEvent.ACTION_DOWN ->
                    points.add(HGPoint(x, y, Paint(pen), false, penMode))
                MotionEvent.ACTION_MOVE -> {
                    when(penMode) {
                        DrawMode.PEN ->
                            points.add(HGPoint(x, y, Paint(pen), true, penMode))
                        DrawMode.CRAYON -> {
//                            val tmp = Paint(pen)
//                            tmp.strokeWidth = 3F
//
//                            points.add(HGPoint(x, y, tmp, true, penMode, makeSmoke(x, y, pen.strokeWidth)))
//                            val linearPoints = getP2P(points[points.size - 1], points[points.size - 2], 7)
//                            for(l in linearPoints) {
//                                points.add(HGPoint(l.x, l.y, tmp, true, penMode, makeSmoke(l.x, l.y, pen.strokeWidth)))
//                            }
                        }
                    }
                }
            }

            invalidate()
            return true
    }

    fun resetCanvas() {
        this.points.clear()
        invalidate()
    }

//    fun getP2P(start: HGPoint, end: HGPoint, delay: Int) : ArrayList<HGPoint> {
//        val distance = Math.abs(start.x - end.x).toInt()
//        val arrow = start.x - end.x > 0            // true = 증감 false - 감소
//
//        val pointArr = arrayListOf<HGPoint>()
//
//        for(i in 0 .. distance step delay) {
//            val x = if(arrow) start.x + i else start.x - i
//            val y = start.y + (end.y - start.y) * (x - start.x) / (end.x - start.x)     // 선형 보간
//            val tmp = Paint(pen)
//            tmp.strokeWidth = 3F
//            pointArr.add(HGPoint(x, y, tmp))
//        }
//
//        return pointArr
//    }
//
//    private val rd = Random()                       // 크레용 모드 렌덤
//    private fun makeSmoke(x: Float, y: Float, radius: Float) : ArrayList<XY> {
//        val points = arrayListOf<XY>()
//        for(i in 0 .. 5)
//            points += XY(rd.nextInt(radius.toInt()) + (x - radius / 2), rd.nextInt(radius.toInt()) + (y - radius / 2))
//        return points
//    }
}