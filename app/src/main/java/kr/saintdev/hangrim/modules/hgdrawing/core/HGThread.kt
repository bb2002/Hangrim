package kr.saintdev.hangrim.modules.hgdrawing.core

import android.graphics.Canvas
import android.graphics.Path
import android.view.SurfaceHolder
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGPoint
import java.util.*

class HGThread(val holder: SurfaceHolder, val surfaceView: HGSurfaceView) : Thread() {
    private val pointArray = Collections.synchronizedList(ArrayList<HGPoint>())
    private var isRunning = true

    override fun run() {
        // Init placeholder
        surfaceView.setPlaceHolder(surfaceView.property.placeholderText)

        while(true) {
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if(canvas != null) {
                    synchronized(holder) {
                        // request draw call.
                        canvas.drawARGB(255, 255, 255, 255)
                        drawCallPlaceholder(canvas)
                        drawCall(canvas)
                    }
                }
            } catch(ex: Exception) {
                isRunning = false
                interrupt()
            } finally {
                if(canvas != null) holder.unlockCanvasAndPost(canvas)
            }

            if(!isRunning) break
        }
    }

    private val path = Path()
    private fun drawCall(canvas: Canvas) {
        synchronized(pointArray) {
            for(i in 1 until pointArray.size) {
                val p = this.pointArray[i]
                if(!p.isDraw || p.isUndo) continue

                path.moveTo(pointArray[i - 1].x, pointArray[i - 1].y)
                path.lineTo(p.x, p.y)
                canvas.drawPath(path, p.paint)
                path.reset()
            }
        }
    }

    private fun drawCallPlaceholder(canvas: Canvas) {
        val text = surfaceView.getPlaceHolderText()
        val paint = surfaceView.getPlaceHolderPaint()
        if(text != null && paint != null) {
            canvas.drawText(text, surfaceView.getPlaceHolderX(), surfaceView.getPlaceHolderY(), paint)
        }
    }

    /**
     * @Date 01.05 2019
     * Add draw call point
     */
    fun addDrawCallPoint(point: HGPoint) {
        synchronized(pointArray) {
            pointArray.add(point)
        }
    }

    /**
     * @Date 01.07 2019
     * Clear draw call points
     */
    fun clearDrawCallPoint() {
        synchronized(pointArray) {
            pointArray.clear()
        }
    }

    fun getDrawCallPoints() = this.pointArray

    /**
     * @Date 12.28 2018
     * Undo Point
     */
    fun undoPoint() : Boolean {
        if(pointArray.isEmpty()) return false

        for(i in pointArray.size-1 downTo 0) {
            val p = pointArray[i]
            if(p.isDraw && !p.isUndo) {
                // Undo 처리 한다.
                p.isUndo = true
                return true
            }
        }

        return false
    }

    /**
     * @Date 12.28 2018
     * Redo point
     */
    fun redoPoint() : Boolean {
        if(pointArray.isEmpty()) return false

        for(i in 0 until pointArray.size) {
            val p = pointArray[i]
            if(p.isDraw && p.isUndo) {
                // Redo 처리 한다.
                p.isUndo = false
                return true
            }
        }

        return false
    }

    /**
     * @Date 01.05 2019
     * Running settings
     */
    fun setRunning(b: Boolean) {
        this.isRunning = b
    }
}