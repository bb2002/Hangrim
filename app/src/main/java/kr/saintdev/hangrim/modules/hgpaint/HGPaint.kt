package kr.saintdev.hangrim.modules.hgpaint

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgpaint.canvas.HGCanvasView

class HGPaint : RelativeLayout {
    private lateinit var hgView: RelativeLayout

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
        val drawView = HGCanvasView(context)
        val hgCont = this.hgView.findViewById<RelativeLayout>(R.id.hg_paint_container)

        // Init Attr
        if(attrs != null) {
            val tArr = context.obtainStyledAttributes(attrs, R.styleable.HGPaint)

            // set placeholder text
            val placeholderText = tArr.getString(R.styleable.HGPaint_placeholder)
            drawView.placeholderText = placeholderText

            // set canvas size
            val canvasWidth = tArr.getInt(R.styleable.HGPaint_canvasWidth, 0)
            val canvasHeight = tArr.getInt(R.styleable.HGPaint_canvasHeight, 0)
            drawView.layoutParams = ViewGroup.LayoutParams(canvasWidth, canvasHeight)

            // set Divide line
            val divideLineArr = booleanArrayOf(
                    tArr.getBoolean(R.styleable.HGPaint_useDivideLeft, false),
                    tArr.getBoolean(R.styleable.HGPaint_useDivideRight, false),
                    tArr.getBoolean(R.styleable.HGPaint_useDivideTop, false),
                    tArr.getBoolean(R.styleable.HGPaint_useDivideBottom, false)
            )

            val divideLineRef = arrayOf<ImageView>(
                    this.hgView.findViewById(R.id.hg_paint_divide_left),
                    this.hgView.findViewById(R.id.hg_paint_divide_right),
                    this.hgView.findViewById(R.id.hg_paint_divide_top),
                    this.hgView.findViewById(R.id.hg_paint_divide_bottom)
            )

            for(i in 0 until divideLineArr.size) {
                divideLineRef[i].visibility = if(divideLineArr[i]) View.VISIBLE else View.INVISIBLE
            }

            // Set bottom comment
            this.hgView.findViewById<TextView>(R.id.hg_paint_comment_title).text = tArr.getString(R.styleable.HGPaint_commentTitle)
            this.hgView.findViewById<TextView>(R.id.hg_paint_comment_content).text = tArr.getString(R.styleable.HGPaint_commentContent)
            tArr.recycle()
        }

        hgCont.addView(drawView)

        // Add view
        this.addView(this.hgView)
    }
}