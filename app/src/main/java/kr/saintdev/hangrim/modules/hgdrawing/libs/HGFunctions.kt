package kr.saintdev.hangrim.modules.hgdrawing.libs

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.gdacciaro.iOSDialog.iOSDialogBuilder
import com.gdacciaro.iOSDialog.iOSDialogClickListener
import java.lang.Exception

object HGConvert {
    fun pxToDpi(value: Int, context: Context) =
        if(value == -1 || value == -2) value
        else TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics).toInt()
}

object HGDialog {
    fun openAlert(title: Int, content: Int, context: Context) {
        try {
            val activity = context as AppCompatActivity

            iOSDialogBuilder(activity)
                .setTitle(context.getString(title))
                .setSubtitle(context.getString(content))
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("OK") { dialog -> dialog.dismiss() }
                .build().show()
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun openConfirm(title: Int, content: Int, context: Context,
                    posListener: iOSDialogClickListener = iOSDialogClickListener{ dialog -> dialog.dismiss() },
                    navListener: iOSDialogClickListener = iOSDialogClickListener{ dialog -> dialog.dismiss() }
    ) {
        try {
            val activity = context as AppCompatActivity

            iOSDialogBuilder(activity)
                .setTitle(context.getString(title))
                .setSubtitle(context.getString(content))
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("Yes", posListener)
                .setNegativeListener("No", navListener)
                .build().show()
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }
}