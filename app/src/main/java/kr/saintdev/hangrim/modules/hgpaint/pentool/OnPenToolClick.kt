package kr.saintdev.hangrim.modules.hgpaint.pentool

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kr.saintdev.hangrim.R
import android.view.Gravity
import android.R.attr.gravity
import android.graphics.Color
import android.view.ViewGroup
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.hgpaint.canvas.HGCanvasView
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGDefaultPaint
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGPenSize
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenColor
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenStyleDotted
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenStyleStroke
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenThickness
import kr.saintdev.hangrim.modules.hgpaint.pentool.reset.ResetTool


class OnPenToolClick(
    private val context: Context,
    private val rootView: RelativeLayout,
    val hgPaint: HGPaint, val hgCanvas: HGCanvasView
) : View.OnClickListener {

    private val defaultCloseButton: ImageButton             // 기본 닫기 버튼
    private val toolkitView: LinearLayout                   // 툴킷 컨텍스트 뷰
    private val toolKitContextRoot: FrameLayout
    private val toolKitItem = arrayListOf<ToolButton>()     // 툴킷에 들어가야할 버튼

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.toolkitView = inflater.inflate(R.layout.hg_paint_tool_items, rootView, false) as LinearLayout
        this.toolKitContextRoot = this.rootView.findViewById(R.id.hg_paint_tool_context)
        this.defaultCloseButton = this.toolkitView.findViewById<ImageButton>(R.id.hg_painttool_close)
        this.defaultCloseButton.setOnClickListener {
            toolkitContextClose()
        }
    }

    override fun onClick(v: View) {
        toolkitContextClose()       // 현재 열린것을 닫고

        val openContextMenu = when(v.id) {
            R.id.hg_paint_tool_penstyle -> {
                this.toolKitItem.add(PenStyleStroke(context, this))
                this.toolKitItem.add(PenStyleDotted(context, this))
                true
            }
            R.id.hg_paint_tool_penthickness -> {
                this.toolKitItem.add(PenThickness(context, this, HGPenSize.HG_THIN.size))
                this.toolKitItem.add(PenThickness(context, this, HGPenSize.HG_NORMAL.size))
                this.toolKitItem.add(PenThickness(context, this, HGPenSize.HG_THICK.size))
                true
            }
            R.id.hg_paint_tool_reset -> {
                ResetTool.onReset(context, this)
                false
            }
            R.id.hg_paint_tool_pencolor -> {
                this.toolKitItem.add(PenColor(context, this, Color.rgb(192, 57, 43)))
            }
            else -> {
                false
            }
        }

        if(openContextMenu) toolkitContextOpen()        // 새로운것을 연다.

        for(i in this.toolKitItem) {
            this.toolkitView.addView(i)
        }
    }






    private fun toolkitContextClose() {
        try {
            this.toolKitContextRoot.removeView(this.toolkitView)
            this.toolKitItem.forEach {  // ToolButton 을 제거 한다.
                this.toolkitView.removeView(it)
            }
            this.toolKitItem.clear()    // ToolButton 을 Clear 한다.

            this.toolKitContextRoot.visibility = View.GONE
        } catch(ex: Exception) { }
    }

    private fun toolkitContextOpen() {
        try {
            this.toolKitContextRoot.addView(this.toolkitView)
            this.toolKitContextRoot.visibility = View.VISIBLE
        } catch(ex: Exception) { }
    }
}