package kr.saintdev.hangrim.modules.hgpaint.pentool

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kr.saintdev.hangrim.R
import android.view.Gravity
import android.R.attr.gravity
import android.view.ViewGroup


class OnPenToolClick : View.OnClickListener {

    private var defaultCloseButton: ImageButton     // 기본 닫기 버튼
    private var toolkitView: View                   // 툴킷 컨텍스트 뷰
    private val context: Context        // context
    private val rootView: RelativeLayout    // rootView
    private val toolKitContextRoot: FrameLayout

    constructor(context: Context, rootView: RelativeLayout) {
        this.context = context
        this.rootView = rootView

        // Init toolkit context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.toolkitView = inflater.inflate(R.layout.hg_paint_tool_items, rootView, false) as LinearLayout
        this.toolKitContextRoot = this.rootView.findViewById(R.id.hg_paint_tool_context)

        // init close button
        this.defaultCloseButton = this.toolkitView.findViewById<ImageButton>(R.id.hg_painttool_close)
        this.defaultCloseButton.setOnClickListener {
            toolkitContextClose()
        }
    }

    override fun onClick(v: View) {
        toolkitContextOpen()
    }

    private fun toolkitContextClose() {
        this.toolKitContextRoot.removeView(this.toolkitView)
        this.toolKitContextRoot.visibility = View.GONE
    }

    private fun toolkitContextOpen() {
        this.toolKitContextRoot.addView(this.toolkitView)
        this.toolKitContextRoot.visibility = View.VISIBLE
    }
}