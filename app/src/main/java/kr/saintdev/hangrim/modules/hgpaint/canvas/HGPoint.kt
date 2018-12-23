package kr.saintdev.hangrim.modules.hgpaint.canvas

import android.graphics.Paint

/**
 * Default Draw Point
 */
data class HGPoint (
        val x: Float = 0.0F,
        val y: Float = 0.0F,
        val paint: Paint,
        val isDraw: Boolean = false,
        val drawMode: DrawMode = DrawMode.PEN
)

enum class DrawMode {
        PEN,
        CRAYON
}