package kr.saintdev.hangrim.modules.hgpaint

import android.content.Context
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.pxToDpi
import kr.saintdev.hangrim.modules.hgpaint.canvas.HGCanvasSurface
import kr.saintdev.hangrim.modules.hgpaint.pentool.OnPenToolClick
import kr.saintdev.hangrim.modules.hgpaint.toolbar.HGToolbarTool
import kr.saintdev.hangrim.modules.hgpaint.toolbar.OnToolClick
import java.io.File

class HGPaint : RelativeLayout {
    private lateinit var hgView: RelativeLayout         // PaintBoard Layout
    private lateinit var hgCanvasView: HGCanvasSurface  // PaintBoard Canvas

    constructor(context: Context) : super(context) {
        initView(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }


    /**
     * Create HGPaint
     * Init View
     */
    private fun initView(context: Context, attrs: AttributeSet? = null) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.hgView = inflater.inflate(R.layout.hg_paint_board, this, false) as RelativeLayout

        // Create Canvas
        val hgCont = this.hgView.findViewById<RelativeLayout>(R.id.hg_paint_container)

        // Init Attr
        if(attrs != null) {
            val tArr = context.obtainStyledAttributes(attrs, R.styleable.HGPaint)

            /**
             * Set canvas layout
             */
            this.hgView.findViewById<TextView>(R.id.hg_paint_title).text = tArr.getString(R.styleable.HGPaint_title)

            drawDivideLine(
                tArr.getBoolean(R.styleable.HGPaint_useDivideLeft, false),
                tArr.getBoolean(R.styleable.HGPaint_useDivideRight, false),
                tArr.getBoolean(R.styleable.HGPaint_useDivideTop, false),
                tArr.getBoolean(R.styleable.HGPaint_useDivideBottom, false)
            )

            drawCommentMessage(tArr.getString(R.styleable.HGPaint_commentTitle), tArr.getString(R.styleable.HGPaint_commentContent))

            /**
             * Set canvas surface view
             */
            val placeholderText = tArr.getString(R.styleable.HGPaint_placeholder)             // get Placeholder
            val canvasWidth = tArr.getInt(R.styleable.HGPaint_canvasWidth, 0)       // get width
            val canvasHeight = tArr.getInt(R.styleable.HGPaint_canvasHeight, 0)     // get height

            startCanvasThread(canvasWidth, canvasHeight, placeholderText)                     // start canvas
            tArr.recycle()
        }

        // Load pentool
        val pentoolButtons = arrayOf<ImageButton>(
                this.hgView.findViewById(R.id.hg_paint_tool_penstyle),          // 팬 스타일 지정
                this.hgView.findViewById(R.id.hg_paint_tool_penthickness),      // 팬 두께 지정
                this.hgView.findViewById(R.id.hg_paint_tool_reset),            // 리셋으로 사용
                this.hgView.findViewById(R.id.hg_paint_tool_pencolor)           // 색상 선택
        )

        val toolClickListener = OnPenToolClick(context, this.hgView, this.hgCanvasView)
        for(i in pentoolButtons) i.setOnClickListener(toolClickListener)

        hgCont.addView(this.hgCanvasView)

        // Add view
        this.addView(this.hgView)
    }

    private fun startCanvasThread(width: Int, height: Int, placeHolder: String?) {
        this.hgCanvasView = HGCanvasSurface(placeHolder, context)
        this.hgCanvasView.layoutParams = ViewGroup.LayoutParams(width.pxToDpi(context), height.pxToDpi(context))
    }



    /**
     * 12.25 2018
     * 구분선을 그립니다.
     * @param left, right, top, bottom
     */
    private fun drawDivideLine(vararg options: Boolean) {
        val ref = arrayOf<ImageView>(
            this.hgView.findViewById(R.id.hg_paint_divide_left),
            this.hgView.findViewById(R.id.hg_paint_divide_right),
            this.hgView.findViewById(R.id.hg_paint_divide_top),
            this.hgView.findViewById(R.id.hg_paint_divide_bottom)
        )

        for(i in 0 until options.size) {
            ref[i].visibility = if(options[i]) View.VISIBLE else View.INVISIBLE
        }
    }

    /**
     * 12.25 2018
     * Message 를 그립니다.
     * @param title 제목
     * @param submsg 작은 메세지
     */
    private fun drawCommentMessage(title: String?, submsg: String?) {
        if(title != null)
            this.hgView.findViewById<TextView>(R.id.hg_paint_comment_title).text = title

        if(submsg != null)
            this.hgView.findViewById<TextView>(R.id.hg_paint_comment_content).text = submsg
    }


    fun setPlaceHolderText(text: String) {
        this.hgCanvasView.placeHolder = text
    }

    fun setComment(title: String?, msg: String?) = drawCommentMessage(title, msg)

    /**
     * 12.28 2018
     * 해당 액티비티의 Toolbar 에 HGToolbar 를 적용합니다.
     */
    private lateinit var hgPaintToolBar: View
    fun setHGPaintToolbar(activity: AppCompatActivity) {
        val toolbar = activity.supportActionBar
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.hgPaintToolBar = inflater.inflate(R.layout.hg_paint_toolbar, null, false)

        toolbar?.setDisplayShowCustomEnabled(true)
        toolbar?.setCustomView(
            this.hgPaintToolBar,
            ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )

        // toolbar 의 undo / redo 활성화
        val btns = arrayOf<ImageButton>(
            this.hgPaintToolBar.findViewById(R.id.hg_paint_toolbar_undo),
            this.hgPaintToolBar.findViewById(R.id.hg_paint_toolbar_redo)
        )

        btns[0].setOnClickListener {
            if(!this.hgCanvasView.unredo(true)) {
                R.string.common_warn.alert(R.string.hgpaint_undo_fail, activity)
            }
        }

        btns[1].setOnClickListener {
            if(!this.hgCanvasView.unredo(false)) {
                R.string.common_warn.alert(R.string.hgpaint_redo_fail, activity)
            }
        }
    }

    fun setHGPaintToolListener(listener: OnToolClick) {
        val btns = arrayOf<ImageButton>(
            this.hgPaintToolBar.findViewById(R.id.hg_paint_toolbar_next),
            this.hgPaintToolBar.findViewById(R.id.hg_paint_toolbar_back)
        )

        val clickListener = View.OnClickListener {
            val tool = when(it.id) {
                R.id.hg_paint_toolbar_next -> HGToolbarTool.FORWARD
                R.id.hg_paint_toolbar_back -> HGToolbarTool.BACKWARD
                else -> HGToolbarTool.FORWARD
            }

            listener.onClick(tool, this@HGPaint)
        }

        for(b in btns) b.setOnClickListener(clickListener)
    }

    /**
     * @Date 12.28 2018
     * Export image
     */
    fun exportImage(filename: String) =
            this.hgCanvasView.exportDrawing(filename)

    fun exit() {
        this.hgCanvasView.clear()
        this.hgCanvasView.release()
        this.hgCanvasView.visibility = View.GONE
    }
}