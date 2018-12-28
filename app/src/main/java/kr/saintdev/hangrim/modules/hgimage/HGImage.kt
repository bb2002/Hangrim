package kr.saintdev.hangrim.modules.hgimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import java.io.File

object HGImage {
    /**
     * @Date 12.28 2018
     *
     */
    fun attachShuffleAndDrawing(shuffle: File, picture: File) : Bitmap {
        var shuffleBitmap = BitmapFactory.decodeFile(shuffle.absolutePath)
        var pictureBitmap = BitmapFactory.decodeFile(picture.absolutePath)
        val newBitmap = Bitmap.createBitmap(shuffleBitmap.width, shuffleBitmap.height + pictureBitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint()

        val canvas = Canvas(newBitmap)

        // shuffle bitmap 을 resize 합니다.
        canvas.drawBitmap(pictureBitmap, 0F, 0F, paint)
        canvas.drawBitmap(shuffleBitmap, 0F, pictureBitmap.height.toFloat(), paint)
        return newBitmap
    }
}