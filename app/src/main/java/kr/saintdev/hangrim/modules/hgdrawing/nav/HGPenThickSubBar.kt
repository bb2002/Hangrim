package kr.saintdev.hangrim.modules.hgdrawing.nav

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgdrawing.core.HGSurfaceView
import kr.saintdev.hangrim.modules.hgdrawing.property.PenSize

class HGPenThickSubBar : LinearLayout, View.OnClickListener {
    private lateinit var rootView: LinearLayout

    private lateinit var thicknessButtons: Array<ImageButton>
    private val thicknessButtonSelectRes = arrayOf(
        R.drawable.ic_hgpaint_burshsize1_selected,
        R.drawable.ic_hgpaint_burshsize2_selected,
        R.drawable.ic_hgpaint_burshsize3_selected
    )
    private val thicknessButtonNonRes = arrayOf(
        R.drawable.ic_hgpaint_burshsize1_none,
        R.drawable.ic_hgpaint_burshsize2_none,
        R.drawable.ic_hgpaint_burshsize3_none
    )
    private var surfaceView: HGSurfaceView

    constructor(context: Context?, surfaceView: HGSurfaceView) : super(context) {
        this.surfaceView = surfaceView
        initView()
    }

    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.rootView = inflater.inflate(R.layout.hgpaint_sub_toolbar_tickness, this, false) as LinearLayout

        this.thicknessButtons = arrayOf(
            this.rootView.findViewById(R.id.hgpaint_thickness_1),
            this.rootView.findViewById(R.id.hgpaint_thickness_2),
            this.rootView.findViewById(R.id.hgpaint_thickness_3))

        for(b in this.thicknessButtons) b.setOnClickListener(this)

        val selectedPen =
            when(surfaceView.paint.strokeWidth) {
                PenSize.HG_THIN.size ->         R.id.hgpaint_thickness_1
                PenSize.HG_NORMAL.size ->       R.id.hgpaint_thickness_2
                PenSize.HG_THICK.size ->        R.id.hgpaint_thickness_3
                else -> R.id.hgpaint_thickness_2
            }
        onSelected(selectedPen)

        this.addView(this.rootView)
    }

    private fun onSelected(id: Int) {
        for(i in 0 .. 2) thicknessButtons[i].setImageResource(thicknessButtonNonRes[i])

        val idx = when(id) {
            R.id.hgpaint_thickness_1 -> 0
            R.id.hgpaint_thickness_2 -> 1
            R.id.hgpaint_thickness_3 -> 2
            else -> 0
        }
        thicknessButtons[idx].setImageResource(thicknessButtonSelectRes[idx])
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.hgpaint_thickness_1 -> surfaceView.paint.strokeWidth = PenSize.HG_THIN.size
            R.id.hgpaint_thickness_2 -> surfaceView.paint.strokeWidth = PenSize.HG_NORMAL.size
            R.id.hgpaint_thickness_3 -> surfaceView.paint.strokeWidth = PenSize.HG_THICK.size
        }

        onSelected(v.id)
    }
}