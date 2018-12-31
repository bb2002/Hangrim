package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.*
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import java.io.File
import java.io.FileOutputStream
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
            this.hgThread.stopThread()
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

    fun unredo(undo: Boolean) = if(undo) this.hgThread.undoPoint() else this.hgThread.redoPoint()

    /**
     * @Date 12.28 2018
     * Drawing 데이터를 이미지로 출력합니다.
     */
    fun exportDrawing(filename: String) : File? {
        val points = this.hgThread.exportDrawingPoints()
        val drawPath = Path()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val filePath = HGFunctions.getTempFileLocation(filename, context)

        // Draw White
        canvas.drawARGB(255, 255, 255, 255)

        // Draw User image
        for (i in 1 until points.size) {
            val p = points[i]
            if (!p.isDraw) continue

            drawPath.moveTo(points[i - 1].x, points[i - 1].y)
            drawPath.lineTo(p.x, p.y)
            canvas.drawPath(drawPath, p.paint)
            drawPath.reset()
        }

        val fos: FileOutputStream
        return try {
            // Create Bitmap file
            fos = FileOutputStream(filePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.close()
            filePath
        } catch(ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    /**
     * @Date 12.30 2018
     * Canvas surface release
     */
    fun release() {
        try {
            this.hgThread.stopThread()
            holder.removeCallback(this)
        } catch(ex: Exception) {}
    }
}