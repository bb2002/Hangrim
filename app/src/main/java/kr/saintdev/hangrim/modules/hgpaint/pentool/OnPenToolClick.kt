package kr.saintdev.hangrim.modules.hgpaint.pentool

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kr.saintdev.hangrim.R
import android.graphics.Color
import kr.saintdev.hangrim.modules.hgpaint.HGPaint
import kr.saintdev.hangrim.modules.hgpaint.canvas.HGCanvasView
import kr.saintdev.hangrim.modules.hgpaint.hglibs.HGPenSize
import kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`.PenColors
import kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`.PenTool
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenColor
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenStyleStroke
import kr.saintdev.hangrim.modules.hgpaint.pentool.pen.PenThickness
import kr.saintdev.hangrim.modules.hgpaint.pentool.`fun`.ResetTool


class OnPenToolClick(
    private val context: Context,
    private val rootView: RelativeLayout,
    val hgPaint: HGPaint, val hgCanvas: HGCanvasView
) : View.OnClickListener {
    private val contextMenuRoot: FrameLayout =
        rootView.findViewById(R.id.hg_paint_tool_context)                // Context menu 의 Root
    private val contextMenuContainer: LinearLayout          // Context menu 컨테이너
    private val contextMenuItemContainer: LinearLayout      // context menu 의 아이템

    private val toolKitItem = arrayListOf<ToolButton>()     // 툴킷에 들어가야할 버튼
    private val defaultCloseButton: ImageButton             // 기본 닫기 버튼


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.contextMenuContainer = inflater.inflate(R.layout.hg_paint_tool_items, contextMenuRoot, false) as LinearLayout
        this.contextMenuItemContainer = this.contextMenuContainer.findViewById(R.id.hg_painttool_root)

        this.defaultCloseButton = this.contextMenuContainer.findViewById(R.id.hg_painttool_close)
        this.defaultCloseButton.setOnClickListener {
            toolkitContextClose()
        }
    }

    override fun onClick(v: View) {
        toolkitContextClose()       // 현재 열린것을 닫고

        val openContextMenu = when(v.id) {
            R.id.hg_paint_tool_penstyle -> {
                PenTool.onPenChange(context,this)
                false
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
                this.toolKitItem.addAll(PenColors.addBlacks(context, this))
                this.toolKitItem.addAll(PenColors.addColors(context, this))
            }
            else -> {
                false
            }
        }

        if(openContextMenu) toolkitContextOpen()                // Open context menu
        for(i in this.toolKitItem) this.contextMenuItemContainer.addView(i)  // Toolkit cont 에 아이템 추가
    }


    private fun toolkitContextClose() {
        try {
            this.contextMenuRoot.removeView(this.contextMenuContainer)
            this.toolKitItem.forEach {  // ToolButton 을 제거 한다.
                this.contextMenuItemContainer.removeView(it)
            }
            this.toolKitItem.clear()    // ToolButton 을 Clear 한다.

            this.contextMenuRoot.visibility = View.GONE
        } catch(ex: Exception) { }
    }

    private fun toolkitContextOpen() {
        try {
            this.contextMenuRoot.addView(this.contextMenuContainer)
            this.contextMenuRoot.visibility = View.VISIBLE
        } catch(ex: Exception) { }
    }
}