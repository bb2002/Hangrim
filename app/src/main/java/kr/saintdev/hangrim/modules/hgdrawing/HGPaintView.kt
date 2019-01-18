package kr.saintdev.hangrim.modules.hgdrawing

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.vibration
import kr.saintdev.hangrim.modules.hgdrawing.core.HGSurfaceView
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGConvert
import kr.saintdev.hangrim.modules.hgdrawing.nav.HGNavigationBar
import kr.saintdev.hangrim.modules.hgdrawing.property.EtcProperty
import java.io.File

class HGPaintView : RelativeLayout {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }


    private lateinit var rootView: RelativeLayout           // Root View
    private lateinit var canvasContainer: FrameLayout       // Canvas Container
    private lateinit var navigationBar: FrameLayout         // Canvas toolbar container
    private lateinit var navBarRight: FrameLayout           // Canvas sub toolbar right
    private lateinit var navBarBottom: FrameLayout          // Canvas sub toolbar bottom

    private lateinit var actionbarForwardButton: ImageButton    // Forward
    private lateinit var actionbarBackwardButton: ImageButton   // Backward
    private lateinit var actionbarUndoButton: ImageButton       // Undo
    private lateinit var actionbarRedoButton: ImageButton       // Redo

    private lateinit var commentTitleView: TextView             // Comment title
    private lateinit var commentContentView: TextView           // Comment content
    private lateinit var canvasView: HGSurfaceView             // Canvas surface
    fun getSurfaceView() = canvasView
    private var paintViewProperty: TypedArray? = null     // Canvas Attribute

    private fun initView(context: Context, attrs: AttributeSet?) {
        this.paintViewProperty =
                context.obtainStyledAttributes(attrs, R.styleable.HGPaintView)          // Set Attributes
    }

    /**
     * @Date 01.18 2019
     * PaintView Activity is Created.
     */
    fun onCreate() {
        // Find All views.
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Find All containers
        this.rootView = inflater.inflate(R.layout.hgpaint_root_view, this, false) as RelativeLayout
        this.canvasContainer = this.rootView.findViewById(R.id.hgpaint_canvas_container) as FrameLayout
        this.navigationBar = this.rootView.findViewById(R.id.hgpaint_toolbar_container) as FrameLayout
        this.navBarRight = this.rootView.findViewById(R.id.hgpaint_sub_toolbar_right) as FrameLayout
        this.navBarBottom = this.rootView.findViewById(R.id.hgpaint_sub_toolbar_bottom) as FrameLayout

        // Find All Views
        this.commentTitleView = this.rootView.findViewById(R.id.hgpaint_comment_title)
        this.commentContentView = this.rootView.findViewById(R.id.hgpaint_comment_content)

        // Apply property
        applyCanvasProperty()

        // Init navigation bar
        this.navigationBar.addView(HGNavigationBar(context, this))

        // Init action bar
        val toolView = inflater.inflate(R.layout.hgpaint_actionbar, null)
        val toolbar = this.rootView.findViewById<android.support.v7.widget.Toolbar>(R.id.hgpaint_actionbar)
        val activity = context as AppCompatActivity

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayShowCustomEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        // Find All Actionbar buttons
        this.actionbarForwardButton =   toolView.findViewById(R.id.hgpaint_actionbar_forward)
        this.actionbarBackwardButton =  toolView.findViewById(R.id.hgpaint_actionbar_back)
        this.actionbarUndoButton =      toolView.findViewById(R.id.hgpaint_actionbar_undo)
        this.actionbarRedoButton =      toolView.findViewById(R.id.hgpaint_actionbar_redo)

        activity.supportActionBar?.setCustomView(toolView, ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        // Set Paint canvas
        val useAlphaChannel = this.paintViewProperty?.getBoolean(R.styleable.HGPaintView_canvasAlpha, false) ?: false
        val useColorPaint = this.paintViewProperty?.getBoolean(R.styleable.HGPaintView_colorToolEnable, true) ?: true
        this.canvasView = HGSurfaceView(context, useAlphaChannel, useColorPaint)

        val canvasWidth = HGConvert.pxToDpi(this.paintViewProperty?.getInt(R.styleable.HGPaintView_canvasWidth, -1) ?: -1, context)
        val canvasHeight = HGConvert.pxToDpi(this.paintViewProperty?.getInt(R.styleable.HGPaintView_canvasHeight, -1) ?: -1, context)
        this.canvasView.layoutParams = ViewGroup.LayoutParams(canvasWidth, canvasHeight)
        this.canvasContainer.addView(this.canvasView)

        // Set OnClickListeners
        this.actionbarUndoButton.setOnClickListener {
            if (this.canvasView.undoPoint())    EtcProperty.VIBRATION_SIZE.vibration(context)       // No points
            else                                for (i in 0..EtcProperty.UNDO_SIZE) this.canvasView.undoPoint() // Yes points
        }

        this.actionbarRedoButton.setOnClickListener {
            if (this.canvasView.redoPoint())    EtcProperty.VIBRATION_SIZE.vibration(context)       // No points
            else                                for (i in 0..EtcProperty.REDO_SIZE) this.canvasView.undoPoint() // Yes points
        }

        addView(rootView)           // Add view
    }

    /**
     * @Date 01.18 2019
     * PaintView Activity is Resume.
     */
    fun onResume() {
        // Set Placeholder text
        val text = this.paintViewProperty?.getText(R.styleable.HGPaintView_placeholder)

        if(text != null && text.isNotEmpty())
            this.canvasView.setPlaceHolder(text.toString())
    }

    /**
     * @Date 01.18 2019
     * PaintView Activity is Destroy.
     */
    fun onDestroy() {
        this.paintViewProperty?.recycle()
    }

    private fun applyCanvasProperty() {
        val attr = this.paintViewProperty
        if(attr != null) {
            // Apply divide line view.
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_right).visibility =
                    if (attr.getBoolean(R.styleable.HGPaintView_divideVertical, false)) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_left).visibility =
                    if (attr.getBoolean(R.styleable.HGPaintView_divideVertical, false)) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_top).visibility =
                    if (attr.getBoolean(R.styleable.HGPaintView_divideHorizon, false))  View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_bottom).visibility =
                    if (attr.getBoolean(R.styleable.HGPaintView_divideHorizon, false))  View.VISIBLE else View.GONE

            // Apply comment message
            this.commentTitleView.text =    attr.getString(R.styleable.HGPaintView_commentTitle) ?: ""
            this.commentContentView.text =  attr.getString(R.styleable.HGPaintView_commentContent) ?: ""
        }
    }

    fun setCommentMessage(title: String, content: String) {
        this.commentTitleView.text = title
        this.commentContentView.text = content
    }

    fun setPlaceHolderText(text: String) = this.canvasView.setPlaceHolder(text)

    fun addSubToolbarView(gravity: Int, view: View) {
        removeSubToolbarView()
        when(gravity) {
            Gravity.END -> {
                this.navBarRight.addView(view)
                this.navBarRight.visibility = View.VISIBLE
            }

            Gravity.BOTTOM ->{
                this.navBarBottom.addView(view)
                this.navBarBottom.visibility = View.VISIBLE
            }
        }
    }

    fun removeSubToolbarView() {
        this.navBarRight.removeAllViews()
        this.navBarBottom.removeAllViews()
        this.navBarRight.visibility = View.GONE
        this.navBarBottom.visibility = View.GONE
    }

    fun setBackwardListener(listener: View.OnClickListener, icon: Int?) {
        this.actionbarBackwardButton.setOnClickListener(listener)
        if(icon != null) {
            this.actionbarBackwardButton.setImageResource(icon)
        }
    }

    fun setForwardListener(listener: View.OnClickListener, icon: Int?) {
        this.actionbarForwardButton.setOnClickListener(listener)
        if(icon != null) {
            this.actionbarForwardButton.setImageResource(icon)
        }
    }

    /**
     * @Date 01.07 2019
     * Export image file
     */
    fun exportImage(file: File) = canvasView?.exportDrawing(file)
}