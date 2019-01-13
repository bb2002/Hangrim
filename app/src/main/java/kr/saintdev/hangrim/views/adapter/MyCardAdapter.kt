package kr.saintdev.hangrim.views.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.modules.hgimage.HGImage
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.MyExpressWord
import java.io.File

class HangrimWordAdapter(var dataset: List<HangrimWord>, val listener: OnCardClickListener) : RecyclerView.Adapter<CardViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        this.context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.mycard_grid_item, parent, false)

        return CardViewHolder(v)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        // Init view
        val textView = holder.view.findViewById<TextView>(R.id.mycard_grid_title)
        val imageView = holder.view.findViewById<ImageView>(R.id.mycard_grid_image)
        val item = dataset[position]
        val drawingFile = HGFunctions.isExsitDrawingFile(item, context)

        textView.text = item.word_english
        if(drawingFile != null && drawingFile.exists()) {
            // Image is draw
            val bitmap = BitmapFactory.decodeFile(drawingFile.absolutePath)
            imageView.setImageBitmap(HGImage.resizeImageCustom(400, bitmap))
        } else {
            imageView.setImageResource(R.drawable.ic_cardmenu_not_draw)
        }

        // setOnClickListener
        holder.view.setOnClickListener{ listener.onClick(it, position) }
        holder.view.setOnLongClickListener {
            listener.onLongClick(it, position)
            true
        }
    }
}

class MyExpressAdapter(var dataset: List<MyExpressWord>, val listener: OnCardClickListener)  : RecyclerView.Adapter<CardViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        this.context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.mycard_grid_item, parent, false)

        return CardViewHolder(v)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val textView = holder.view.findViewById<TextView>(R.id.mycard_grid_title)
        val imageView = holder.view.findViewById<ImageView>(R.id.mycard_grid_image)
        val item = dataset[position]

        textView.text = item.called
        imageView.setImageBitmap(HGImage.resizeImageCustom(400, BitmapFactory.decodeFile(item.imagePath)))

        // setOnClickListener
        holder.view.setOnClickListener{ listener.onClick(it, position) }
        holder.view.setOnLongClickListener {
            listener.onLongClick(it, position)
            true
        }
    }
}

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view)

interface OnCardClickListener {
    fun onClick(view: View, pos: Int)
    fun onLongClick(view: View, pos: Int) {}
}