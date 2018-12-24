package kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`

import android.app.AlertDialog
import android.content.Context
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick

object ResetTool {
    fun onReset(context: Context, penClick: OnPenToolClick) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.hgpaint_reset_title)
        builder.setMessage(R.string.hgpaint_reset_message)

        builder.setPositiveButton(R.string.common_yes) {
            dialogInterface, _ ->
            // canvas clear.
            penClick.hgCanvas.resetCanvas()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(R.string.common_no) {
            dialogInterface, _ -> dialogInterface.dismiss()
        }
        builder.show()
    }
}