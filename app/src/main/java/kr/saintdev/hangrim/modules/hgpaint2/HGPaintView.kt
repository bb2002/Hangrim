package kr.saintdev.hangrim.modules.hgpaint2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kr.saintdev.hangrim.R

/**
 * Copyright (c) 2015-2019 Saint software All rights reserved.
 * @Date 01.03 2019
 * HG Paint Board 2
 */

class HGPaintView : RelativeLayout {
    private lateinit var rootView: RelativeLayout               // HG Paint View

    private lateinit var commentTitle: TextView
    private lateinit var commentContent: TextView
    private lateinit var toolbarButtons: Array<Button>


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    private fun initView(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.rootView = inflater.inflate(R.layout.hgpaint_root_view, this, false) as RelativeLayout

        this.commentTitle = this.rootView.findViewById(R.id.hgpaint_comment_title)
        this.commentContent = this.rootView.findViewById(R.id.hgpaint_comment_content)
        this.toolbarButtons = arrayOf(
            this.rootView.findViewById(R.id.hgpaint_toolbar_penstyle),
            this.rootView.findViewById(R.id.hgpaint_toolbar_thickness),
            this.rootView.findViewById(R.id.hgpaint_toolbar_reset),
            this.rootView.findViewById(R.id.hgpaint_toolbar_color))

        if(attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.HGPaintView)

            // Set Comment
            val cTitle = attrArray.getString(R.styleable.HGPaintView_comment_title) ?: ""
            val cContent = attrArray.getString(R.styleable.HGPaintView_comment_content) ?: ""
            this.commentTitle.text = cTitle
            this.commentContent.text = cContent

            // Set divide line
            val useLRDivide = attrArray.getBoolean(R.styleable.HGPaintView_divide_left_right, false)
            val useTBDivide = attrArray.getBoolean(R.styleable.HGPaintView_divide_top_bottom, false)
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_top).visibility = if(useTBDivide) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_bottom).visibility = if(useTBDivide) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_left).visibility = if(useLRDivide) View.VISIBLE else View.GONE
            this.rootView.findViewById<ImageView>(R.id.hgpaint_divide_right).visibility = if(useLRDivide) View.VISIBLE else View.GONE


            attrArray.recycle()
        }
    }
}