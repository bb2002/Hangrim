package kr.saintdev.hangrim.libs.func

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.TypedValue


fun Int.pxToDpi(context: Context) : Int {
    return if(this == -1 || this == -2)
        this
    else
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

fun Int.str(context: Context) = context.resources.getString(this)


/**
 * 12.26 2018
 * Alert 창을 발생한다.
 */
fun String.alert(msg: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(this)
    builder.setMessage(msg)
    builder.setPositiveButton("Yes") {
        dialogInterface, _ ->
        dialogInterface.dismiss()
    }
    builder.show()
}