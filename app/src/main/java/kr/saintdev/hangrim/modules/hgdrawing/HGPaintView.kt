package kr.saintdev.hgdrawing.hgdrawing

import android.content.Context
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.modules.hgdrawing.core.HGSurfaceView
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGConvert
import kr.saintdev.hangrim.modules.hgdrawing.libs.HGDialog
import kr.saintdev.hangrim.modules.hgdrawing.nav.HGNavigationBar
import kr.saintdev.hangrim.modules.hgdrawing.property.HGCanvasProperty
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

    private var canvasView: HGSurfaceView? = null           // Canvas surface
    private var hgCanvasProperty = HGCanvasProperty()       // Canvas Property

    private var placeholderText: String? = null             // Placeholder text

    private fun initView(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.rootView = inflater.inflate(R.layout.hgpaint_root_view, this, false) as RelativeLayout
        this.canvasContainer = this.rootView.findViewById(R.id.hgpaint_canvas_container) as FrameLayout
        this.navigationBar = this.rootView.findViewById(R.id.hgpaint_toolbar_container) as FrameLayout
        this.navBarRight = this.rootView.findViewById(R.id.hgpaint_sub_toolbar_right) as FrameLayout
        this.navBarBottom = this.rootView.findViewById(R.id.hgpaint_sub_toolbar_bottom) as FrameLayout

        // attr set
        if(attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.HGPaintView)
            this.rootView.findViewById<TextView>(R.id.hgpaint_comment_title).text = attr.getString(R.styleable.HGPaintView_commentTitle)
            this.rootView.findViewById<TextView>(R.id.hgpaint_comment_content).text = attr.getString(R.styleable.HGPaintView_commentContent)

            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_right).visibility =
                    if(attr.getBoolean(R.styleable.HGPaintView_divideVertical, false)) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_left).visibility =
                    if(attr.getBoolean(R.styleable.HGPaintView_divideVertical, false)) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_top).visibility =
                    if(attr.getBoolean(R.styleable.HGPaintView_divideHorizon, false)) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_bottom).visibility =
                    if(attr.getBoolean(R.styleable.HGPaintView_divideHorizon, false)) View.VISIBLE else View.GONE

            this.hgCanvasProperty.isAlpha = attr.getBoolean(R.styleable.HGPaintView_canvasAlpha, false)
            this.hgCanvasProperty.useColorTool = attr.getBoolean(R.styleable.HGPaintView_colorToolEnable, true)
            this.placeholderText = attr.getString(R.styleable.HGPaintView_placeholder)

            this.hgCanvasProperty.canvasWidth = attr.getInt(R.styleable.HGPaintView_canvasWidth, -1)
            this.hgCanvasProperty.canvasHeight = attr.getInt(R.styleable.HGPaintView_canvasHeight, -1)
            this.hgCanvasProperty.placeholderText = attr.getString(R.styleable.HGPaintView_placeholder) ?: ""

            attr.recycle()
        }

        // hg nav bar set
        this.navigationBar.addView(HGNavigationBar(context, this))

        // init action bar
        val toolView = inflater.inflate(R.layout.hgpaint_actionbar, null)
        initActionBar(toolView)

        addView(rootView)
    }

    private fun initActionBar(view: View) {
        val toolbar = this.rootView.findViewById<android.support.v7.widget.Toolbar>(R.id.hgpaint_actionbar)
        val activity = context as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayShowCustomEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)

        // set instance
        this.actionbarForwardButton =   view.findViewById(R.id.hgpaint_actionbar_forward)
        this.actionbarBackwardButton =  view.findViewById(R.id.hgpaint_actionbar_back)
        this.actionbarUndoButton =      view.findViewById(R.id.hgpaint_actionbar_undo)
        this.actionbarRedoButton =      view.findViewById(R.id.hgpaint_actionbar_redo)

        activity.supportActionBar?.setCustomView(view, ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
    }

    /**
     * @Date 01.05 2019
     * Canvas start.
     */
    fun canvasStart() {
        this.canvasView = HGSurfaceView(context, this.hgCanvasProperty)
        this.canvasView?.layoutParams = ViewGroup.LayoutParams(
            HGConvert.pxToDpi(this.hgCanvasProperty.canvasWidth, context), HGConvert.pxToDpi(this.hgCanvasProperty.canvasHeight, context))
        this.canvasContainer.addView(this.canvasView)

        // set listener
        val surface = getSurfaceView()
        if(surface != null) {
            this.actionbarUndoButton.setOnClickListener {
                if(!surface.undoCanvas()) {
                    HGDialog.openAlert(R.string.hgpaint_unredo_error, R.string.hgpaint_undo_error_content, context)
                }
            }     // Dialog 를 손본다.
            this.actionbarRedoButton.setOnClickListener {
                if(!surface.redoCanvas()) {
                    HGDialog.openAlert(R.string.hgpaint_unredo_error, R.string.hgpaint_redo_error_content, context)
                }
            }
        }
    }

    /**
     * @Date 01.05 2019
     * Canvas start.
     */
    fun canvasStop() {
        this.canvasView?.surfaceStop()
        this.canvasContainer.removeAllViews()
        this.canvasView = null
    }

    /**
     * @Date 01.06 2019
     * Get attrs...
     */
    fun getSurfaceView() = this.canvasView

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

    /**
     * @Date 01.07 2019
     * set placeholder text
     */
    fun setPlaceHolderText(text: String) {
        getSurfaceView()?.setPlaceHolder(text)
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