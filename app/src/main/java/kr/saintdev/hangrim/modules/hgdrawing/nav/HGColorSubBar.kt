package kr.saintdev.hangrim.modules.hgdrawing.nav

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgdrawing.core.HGSurfaceView

class HGColorSubBar : LinearLayout, View.OnClickListener {
    private lateinit var rootView: LinearLayout

    private val colorButtons = arrayOf(
        R.id.hgpaint_sub_toolbar_color1,
        R.id.hgpaint_sub_toolbar_color2,
        R.id.hgpaint_sub_toolbar_color3,
        R.id.hgpaint_sub_toolbar_color4,
        R.id.hgpaint_sub_toolbar_color5,
        R.id.hgpaint_sub_toolbar_color6,
        R.id.hgpaint_sub_toolbar_color7,
        R.id.hgpaint_sub_toolbar_color8,
        R.id.hgpaint_sub_toolbar_color9,
        R.id.hgpaint_sub_toolbar_color10,
        R.id.hgpaint_sub_toolbar_color11,
        R.id.hgpaint_sub_toolbar_color12,
        R.id.hgpaint_sub_toolbar_color13,
        R.id.hgpaint_sub_toolbar_color14,
        R.id.hgpaint_sub_toolbar_color15
    )

    private val colorImageView = arrayOf(
        R.id.hgpaint_sub_toolbar_color1_selected,
        R.id.hgpaint_sub_toolbar_color2_selected,
        R.id.hgpaint_sub_toolbar_color3_selected,
        R.id.hgpaint_sub_toolbar_color4_selected,
        R.id.hgpaint_sub_toolbar_color5_selected,
        R.id.hgpaint_sub_toolbar_color6_selected,
        R.id.hgpaint_sub_toolbar_color7_selected,
        R.id.hgpaint_sub_toolbar_color8_selected,
        R.id.hgpaint_sub_toolbar_color9_selected,
        R.id.hgpaint_sub_toolbar_color10_selected,
        R.id.hgpaint_sub_toolbar_color11_selected,
        R.id.hgpaint_sub_toolbar_color12_selected,
        R.id.hgpaint_sub_toolbar_color13_selected,
        R.id.hgpaint_sub_toolbar_color14_selected,
        R.id.hgpaint_sub_toolbar_color15_selected
    )

    private val itemColors = arrayOf(
        R.color.hgpaintColor1,
        R.color.hgpaintColor2,
        R.color.hgpaintColor3,
        R.color.hgpaintColor4,
        R.color.hgpaintColor5,
        R.color.hgpaintColor6,
        R.color.hgpaintColor7,
        R.color.hgpaintColor8,
        R.color.hgpaintColor9,
        R.color.hgpaintColor10,
        R.color.hgpaintColor11,
        R.color.hgpaintColor12,
        R.color.hgpaintColor13,
        R.color.hgpaintColor14,
        R.color.hgpaintColor15
    )

    private var surfaceView: HGSurfaceView
    private var colorText: TextView

    constructor(context: Context?, surfaceView: HGSurfaceView, colorText: TextView) : super(context) {
        this.surfaceView = surfaceView
        this.colorText = colorText
        initView()
    }

    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.rootView = inflater.inflate(R.layout.hgpaint_sub_toolbar_color, this, false) as LinearLayout

        this.colorButtons.forEach {
            this.rootView.findViewById<CardView>(it).setOnClickListener(this)
        }
        onSelected(colorButtons[surfaceView.selectedColorIndex])

        // Color off
        if(!surfaceView.property.useColorTool) {
            this.rootView.findViewById<View>(R.id.hgpaint_sub_toolbar_yescolor).visibility = View.GONE
        }

        this.addView(this.rootView)
    }

    private fun onSelected(v: Int) {
        // 모두 선택되지 않음으로 설정
        for(i in colorImageView) {
            this.rootView.findViewById<ImageView>(i).setImageBitmap(null)
        }

        val idx = getIndex(v)
        this.rootView.findViewById<ImageView>(colorImageView[idx]).setImageResource(R.drawable.hgpaint_color_selected)
    }

    override fun onClick(v: View) {
        onSelected(v.id)

        // set color
        val idx = getIndex(v.id)
        val color = context.resources.getColor(itemColors[idx])
        surfaceView.paint.color = color
        this.colorText.setTextColor(color)
        surfaceView.selectedColorIndex = idx
    }

    fun getIndex(id: Int) : Int {
        for(i in 0 until this.colorButtons.size) if(this.colorButtons[i] == id) return i
        for(i in 0 until this.colorImageView.size) if(this.colorImageView[i] == id) return i
        return 0
    }
}