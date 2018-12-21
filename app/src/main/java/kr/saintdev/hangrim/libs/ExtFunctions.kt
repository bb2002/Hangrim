package kr.saintdev.hangrim.libs

import android.content.Context
import android.support.annotation.Dimension.DP
import android.util.TypedValue
import android.support.annotation.Dimension.DP



fun Int.dpToPixel(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()