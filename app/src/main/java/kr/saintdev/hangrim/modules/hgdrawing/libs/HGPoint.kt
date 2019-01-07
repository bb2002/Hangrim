package kr.saintdev.hangrim.modules.hgdrawing.libs

import android.graphics.Paint

data class HGPoint(
    val x: Float,
    val y: Float,
    val paint: Paint,
    val isDraw: Boolean,
    var isUndo: Boolean = false
)