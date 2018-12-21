package kr.saintdev.hangrim.modules.hgpaint.pentool.pen

import android.content.Context
import android.view.View
import android.widget.Toast
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.pentool.ToolButton

class PenStyleStroke(context: Context, penToolClick: OnPenToolClick) : ToolButton(context, penToolClick) {
    override fun onClick(p0: View?) {
        Toast.makeText(context, "CLICK", Toast.LENGTH_SHORT).show()
    }
}
