package kr.saintdev.hangrim.modules.hgimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import kr.saintdev.hangrim.libs.func.HGFunctions
import java.io.File

object HGImage {
    /**
     * @Date 12.28 2018
     * Attach text image - drawing
     */
    fun attachShuffleAndDrawing(shuffle: File, picture: File) : Bitmap {
        val shuffleBitmap = BitmapFactory.decodeFile(shuffle.absolutePath)
        val pictureBitmap = BitmapFactory.decodeFile(picture.absolutePath)
        val newBitmap = Bitmap.createBitmap(shuffleBitmap.width, shuffleBitmap.height + pictureBitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint()

        val canvas = Canvas(newBitmap)

        // shuffle bitmap 을 resize 합니다.
        canvas.drawBitmap(pictureBitmap, 0F, 0F, paint)
        canvas.drawBitmap(shuffleBitmap, 0F, pictureBitmap.height.toFloat(), paint)
        return newBitmap
    }

    /**
     * @Date 01.08 2019
     * Attach sign
     */
    fun attachSignInDrawing(drawing: Bitmap, context: Context) : Bitmap {
        val canvas = Canvas(drawing)
        val signBitmap = BitmapFactory.decodeFile(HGFunctions.getSignaturePath(context).absolutePath)

        val resizeWidth = 400F
        val aspectRatio = signBitmap.height.toDouble() / signBitmap.width.toDouble()
        val resizeHeight = resizeWidth * aspectRatio

        val signResizedBitmap = Bitmap.createScaledBitmap(signBitmap, resizeWidth.toInt(), resizeHeight.toInt(), false)

        // Attach canvas
        canvas.drawBitmap(signResizedBitmap, drawing.width - resizeWidth, (drawing.height - signResizedBitmap.height).toFloat(), Paint())
        canvas.save()

        return drawing
    }
}