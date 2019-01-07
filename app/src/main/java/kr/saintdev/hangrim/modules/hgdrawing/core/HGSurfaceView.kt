package kr.saintdev.hangrim.modules.hgdrawing.core

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGPoint
import kr.saintdev.hangrim.modules.hgdrawing.property.DefaultPaint
import kr.saintdev.hangrim.modules.hgdrawing.property.HGCanvasProperty
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class HGSurfaceView(context: Context, val property: HGCanvasProperty = HGCanvasProperty()) : SurfaceView(context), SurfaceHolder.Callback {
    private lateinit var thread: HGThread

    private var placeHolderText: String? = null             // Placeholder
    private var placeHolderX = 0F
    private var placeHolderY = 0F
    private var placeHolderPaint: Paint? = null
    val paint = DefaultPaint.getDefaultPaint()
    var selectedColorIndex = 0

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if(holder != null) {
            // Init canvas THREAD.
            this.thread = HGThread(holder, this)
            this.thread.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        surfaceStop()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val point = when(event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP ->
                HGPoint(x, y, Paint(paint), false)
            MotionEvent.ACTION_MOVE ->
                HGPoint(x, y, Paint(paint), true)
            else ->
                null
        }

        if(point != null) this.thread.addDrawCallPoint(point)
        return true
    }

    /**
     * @Date 01.05 2019
     * HGSurfaceView settings
     */
    fun setPlaceHolder(text: String) {
        this.placeHolderPaint = DefaultPaint.getPlaceHolderPaint(DefaultPaint.getPlaceHolderTextSize(context).toFloat())
        val rect = Rect()
        this.placeHolderPaint?.getTextBounds(text, 0, text.length, rect)


        this.placeHolderX = (width / 2 - rect.width() / 2).toFloat()
        this.placeHolderY = (height / 2 + rect.height() / 2).toFloat()
        this.placeHolderText = text
    }

    fun getPlaceHolderText() = this.placeHolderText

    fun getPlaceHolderX() = this.placeHolderX

    fun getPlaceHolderY() = this.placeHolderY

    fun getPlaceHolderPaint() = this.placeHolderPaint

    fun clearCanvas() = this.thread.clearDrawCallPoint()

    fun undoCanvas() = this.thread.undoPoint()
    fun redoCanvas() = this.thread.redoPoint()

    fun exportDrawing(filename: File) : File? {
        val points = this.thread.getDrawCallPoints()
        val drawPath = Path()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

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
            fos = FileOutputStream(filename)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.close()
            filename
        } catch(ex: Exception) {
            ex.printStackTrace()
            null
        }
    }


    fun surfaceStop() {
        try {
            this.thread.setRunning(false)
            this.thread.interrupt()
        } catch(ex: Exception){}
    }
}