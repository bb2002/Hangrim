package kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`

import android.app.AlertDialog
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick

object PenTool {
    fun onPenChange(context: Context, penClick: OnPenToolClick) {
        val vibService = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibService.vibrate(1000)
    }
}