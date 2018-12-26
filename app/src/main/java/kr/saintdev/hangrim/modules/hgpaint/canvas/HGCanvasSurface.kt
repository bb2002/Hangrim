package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.content.Context
import android.graphics.Paint
import android.view.*
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import java.lang.Exception

class HGCanvasSurface(plsHolder: String?, context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private var hgThread: HGCanvasThread                // Draw Thread

    // HGCanvas Properties
    var pen: Paint = HGDefaultPaint.getDefaultPaint()         // Now Pen
    var placeHolder: String = plsHolder ?: ""
    set(text) {
        field = text
        this.hgThread.resizePlaceholder()
    }

    init {
        holder.addCallback(this)            // Add callback
        this.hgThread = HGCanvasThread(context, holder, this)       // create canvas thread
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        if(this.hgThread.state == Thread.State.TERMINATED) {
            this.hgThread = HGCanvasThread(context, holder, this)       // create canvas thread
        }

        this.hgThread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        try {
            this.hgThread.join()
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val point = when(event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP ->
                HGPoint(x, y, Paint(pen), false)
            MotionEvent.ACTION_MOVE -> {
                HGPoint(x, y, Paint(pen), true)
            }
            else -> null
        }
        if(point != null) this.hgThread.addPoint(point)

        return true
    }

    fun clear() {
        this.hgThread.clearPoints()
    }
}