package kr.saintdev.hangrim.libs

import android.content.Context
import android.support.annotation.Dimension.DP
import android.util.TypedValue
import android.support.annotation.Dimension.DP



fun Int.pxToDpi(context: Context) : Int {
    return if(this == -1 || this == -2)
        this
    else
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}