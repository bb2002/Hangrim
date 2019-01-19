package kr.saintdev.hangrim.views.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.os.Message
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

        var handler: Handler? = null

        textView.text = item.word_english
        if(drawingFile != null && drawingFile.exists()) {
            // Image is draw
            LoadImageTask(imageView, context).execute(drawingFile)
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

class LoadImageTask(val imgView: ImageView, val context: Context) : AsyncTask<File, Void, Bitmap>() {
    override fun doInBackground(vararg p0: File?): Bitmap {
        val file = p0[0]
        if(file != null)
            return HGImage.resizeImageCustom(400, BitmapFactory.decodeFile(file.absolutePath))
        else
            return BitmapFactory.decodeResource(context.resources, R.drawable.ic_cardmenu_not_draw)
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)

        if(result != null)
            imgView.setImageBitmap(result)
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
        LoadImageTask(imageView, context).execute(File(item.imagePath))

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