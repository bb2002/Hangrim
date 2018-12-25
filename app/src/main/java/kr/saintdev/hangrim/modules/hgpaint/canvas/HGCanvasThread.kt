package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.Log
import android.view.SurfaceHolder
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGFontLibrary
import java.lang.Exception
import java.util.*

class HGCanvasThread(val context: Context, val holder: SurfaceHolder, val surface: HGCanvasSurface) : Thread() {
    private var isStopped = false           // is Stopped?
    private val drawPath = Path()           // Path instance
    private val points = Collections.synchronizedList(arrayListOf<HGPoint>())

    private var plsHolderX = 0F
    private var plsHolderY = 0F


    override fun run() {
        super.run()

        // size 계산
        val psHolderTextSize = HGFontLibrary.getTextSize(HGDefaultPaint.getPlaceHolderFontSize(context).toInt(), surface.placeHolder)
        val plsPaint = HGDefaultPaint.getPlaceHolderPaint(context)
        this.plsHolderX = HGFontLibrary.getCenterWidth(surface.width, psHolderTextSize.width()).toFloat()
        this.plsHolderY = (surface.height / 2) - ((plsPaint.descent() + plsPaint.ascent()) / 2)

        while(true) {
            var canvas: Canvas? = null

            try {
                canvas = holder.lockCanvas()
                if (isStopped || canvas == null) break

                synchronized(holder) {
                    canvas.drawARGB(255, 255, 255, 255)

                    // Draw Placeholder Text
                    canvas.drawText(surface.placeHolder, this.plsHolderX, this.plsHolderY, plsPaint)

                    // Draw User image
                    synchronized(points) {
                        for (i in 1 until points.size) {
                            val p = points[i]
                            if (!p.isDraw) continue
                            drawPath.moveTo(points[i - 1].x, points[i - 1].y)
                            drawPath.lineTo(p.x, p.y)
                            canvas.drawPath(drawPath, p.paint)
                            drawPath.reset()
                        }
                    }
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
            } finally {
                if(canvas != null)
                    holder.unlockCanvasAndPost(canvas)
                else
                    isStopped = true
            }
        }

        Log.d("HAM", "OUT")
    }

    /**
     * 12.25 2018
     * Add a new draw call point
     */
    fun addPoint(point: HGPoint) {
        points.add(point)
    }

    /**
     * @Date 12.25 2018
     * Clear draw call point
     */
    fun clearPoints() {
        points.clear()
    }
}