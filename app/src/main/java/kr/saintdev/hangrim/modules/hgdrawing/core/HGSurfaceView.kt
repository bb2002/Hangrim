package kr.saintdev.hangrim.modules.hgdrawing.core

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGPoint
import kr.saintdev.hangrim.modules.hgdrawing.property.DefaultPaint
import kr.saintdev.hangrim.modules.hgimage.HGImage
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import android.opengl.ETC1.getHeight



class HGSurfaceView(context: Context, val isAlpha: Boolean = false, val useColorPaint: Boolean = false) : View(context) {
    private var placeHolderText: String? = null             // Placeholder
    private var placeHolderX = 0F
    private var placeHolderY = 0F
    private var placeHolderPaint: Paint? = null

    val paint = DefaultPaint.getDefaultPaint()
    var selectedColorIndex = 0
    private val pointArray = ArrayList<HGPoint>()




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas != null) {
            // request draw call.
            canvas.drawARGB(255, 255, 255, 255)
            drawCallPlaceholder(canvas)
            drawCall(canvas)
        }
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

        if(point != null) pointArray.add(point)
        invalidate()
        return true
    }

    private val path = Path()
    private fun drawCall(canvas: Canvas) {
        for(i in 1 until pointArray.size) {
            val p = this.pointArray[i]
            if(!p.isDraw || p.isUndo) continue

            path.moveTo(pointArray[i - 1].x, pointArray[i - 1].y)
            path.lineTo(p.x, p.y)
            canvas.drawPath(path, p.paint)
            path.reset()
        }
    }

    private fun drawCallPlaceholder(canvas: Canvas) {
        if(placeHolderText != null && placeHolderPaint != null)
            canvas.drawText(placeHolderText, placeHolderX, placeHolderY, placeHolderPaint)
    }

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

                invalidate()
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
                // Undo 처리 한다.
                p.isUndo = false

                invalidate()
                return true
            }
        }

        return false
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

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                if(placeHolderText != null)
                    setPlaceHolder(placeHolderText!!)
            }
        })

        invalidate()
    }


    fun clearCanvas() {
        pointArray.clear()
        invalidate()
    }

    fun exportDrawing(filename: File) : File? {
        val drawPath = Path()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw White
        canvas.drawARGB(if(this.isAlpha) 0 else 255, 255, 255, 255)

        // Draw User image
        for (i in 1 until pointArray.size) {
            val p = pointArray[i]
            if (!p.isDraw || p.isUndo) continue

            drawPath.moveTo(pointArray[i - 1].x, pointArray[i - 1].y)
            drawPath.lineTo(p.x, p.y)
            canvas.drawPath(drawPath, p.paint)
            drawPath.reset()
        }

        val fos: FileOutputStream
        return try {
            // Create Bitmap file
            fos = FileOutputStream(filename)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()

            filename
        } catch(ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}