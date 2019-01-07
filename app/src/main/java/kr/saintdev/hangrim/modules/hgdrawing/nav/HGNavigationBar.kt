package kr.saintdev.hangrim.modules.hgdrawing.nav

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import kr.saintdev.hgdrawing.hgdrawing.HGPaintView
import com.gdacciaro.iOSDialog.iOSDialogClickListener
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGDialog.openConfirm


class HGNavigationBar : LinearLayout, View.OnClickListener {
    private lateinit var rootView: LinearLayout

    private lateinit var toolbarItem: Array<ImageButton>        // Toolbar image button
    private lateinit var colorTextView: TextView                // Color text color
    private lateinit var hgPaintView: HGPaintView

    constructor(context: Context?, hgPaintView: HGPaintView) : super(context) {
        this.hgPaintView = hgPaintView
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){ initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) { initView() }

    private fun initView() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.rootView = inflater.inflate(R.layout.hgpaint_toolbar_layout, this, false) as LinearLayout

        this.toolbarItem = arrayOf(
            this.rootView.findViewById(R.id.hgpaint_toolbar_brush),
            this.rootView.findViewById(R.id.hgpaint_toolbar_thickness),
            this.rootView.findViewById(R.id.hgpaint_toolbar_reset),
            this.rootView.findViewById(R.id.hgpaint_toolbar_color)
        )
        this.toolbarItem.forEach { it.setOnClickListener(this) }

        this.colorTextView = this.rootView.findViewById(R.id.hgpaint_toolbar_color_text)

        this.addView(this.rootView)
    }

    /**
     * @Date 01.06 2019
     * Toolbar click listener
     */
    var nowSelectedIdx = 0
    override fun onClick(v: View) {
        val surface = hgPaintView.getSurfaceView()

        if(v.id == nowSelectedIdx) {
            this.hgPaintView.removeSubToolbarView()
            this.nowSelectedIdx = 0
        } else {
            if (surface != null) {
                when (v.id) {
                    R.id.hgpaint_toolbar_brush -> {
                        // 진동 포함 작업을 필요로 한다.
                    }
                    R.id.hgpaint_toolbar_thickness -> {
                        this.hgPaintView.addSubToolbarView(Gravity.END, HGPenThickSubBar(context, surface))
                        nowSelectedIdx = R.id.hgpaint_toolbar_thickness
                    }
                    R.id.hgpaint_toolbar_reset -> {
                        openConfirm(R.string.hgpaint_reset_title, R.string.hgpaint_reset_content, context, iOSDialogClickListener {
                            surface.clearCanvas()       // Clear canvas
                            it.dismiss()
                        })
                    }
                    R.id.hgpaint_toolbar_color -> {
                        this.hgPaintView.addSubToolbarView(Gravity.BOTTOM, HGColorSubBar(context, surface, colorTextView))
                        nowSelectedIdx = R.id.hgpaint_toolbar_color
                    }
                }
            }
        }
    }
}