package kr.saintdev.hangrim.views.activities.list

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.mycard_category_items.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.libs.sql.SQLManager
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.MyExpressWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyCardActivity : AppCompatActivity() {
    private lateinit var gridViewAdapter: BaseAdapter
    private lateinit var categoryButtons: Array<View>
    private lateinit var categoryButtonText: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        // 서버로 부터 해당 카테고리의 단어를 가져온다.
        requestWordFromServer("Object")

        // set listener
        my_card_grid.onItemClickListener =  AdapterView.OnItemClickListener {
            _, _, pos, id ->
            Toast.makeText(this, "$pos is click!", Toast.LENGTH_SHORT).show()
        }

        this.categoryButtons = arrayOf(
            mycard_cate_0, mycard_cate_1, mycard_cate_2,
            mycard_cate_3, mycard_cate_4, mycard_cate_5,
            mycard_cate_6, mycard_cate_7, mycard_cate_8,
            mycard_cate_9, mycard_cate_10 )

        this.categoryButtonText = arrayOf(
            mycard_cate_0_title, mycard_cate_1_title, mycard_cate_2_title,
            mycard_cate_3_title, mycard_cate_4_title, mycard_cate_5_title,
            mycard_cate_6_title, mycard_cate_7_title, mycard_cate_8_title,
            mycard_cate_9_title, mycard_cate_10_title )

        val listener = OnNavClickListener()
        for(btn in this.categoryButtons) btn.setOnClickListener(listener)

        setSupportActionBar(mycard_toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mycard_go_home.setOnClickListener { finish() }

        // 기본값 선택
        onNavClickUpdate(mycard_cate_0)
    }

    /**
     * @Date 01.01 2019
     * Request from server.
     */
    private fun requestWordFromServer(category: String) {
        val service = Retrofit.getRetrofit().create(HangrimService::class.java)
        val request = service.requestAllWords(category)
        request.enqueue(ServerCallBack())
        mycard_progress.showProgressBar()
    }

    /**
     * @Date 01.01 2019
     * Reset Adapter
     */
    private fun resetAdapter(words: List<HangrimWord>) {
        if(words.isEmpty()) {
            my_card_grid.visibility = View.GONE
        } else {
            my_card_grid.visibility = View.VISIBLE
            this.gridViewAdapter = GirdAdapter(applicationContext, words)
            my_card_grid.adapter = this.gridViewAdapter
        }
    }

    private fun resetAdapterForExpress(words: List<MyExpressWord>) {
        if(words.isEmpty()) {
            my_card_grid.visibility = View.GONE
        } else {
            my_card_grid.visibility = View.VISIBLE
            this.gridViewAdapter = MyExpressGridAdapter(applicationContext, words)
            my_card_grid.adapter = this.gridViewAdapter
        }
    }

    /**
     * @Date 01.02 2019
     * Nav click update
     */
    private fun onNavClickUpdate(v: View) {
        this.categoryButtonText.forEach { it.visibility = View.INVISIBLE }

        for(i in 0 until this.categoryButtons.size)
            if(v.id == this.categoryButtons[i].id) this.categoryButtonText[i].visibility = View.VISIBLE
    }



    /**
     * @Date 01.01 2019
     * Callback from server.
     */
    inner class ServerCallBack : Callback<List<HangrimWord>> {
        override fun onFailure(call: Call<List<HangrimWord>>?, t: Throwable?) {
            mycard_progress.hideProgressBar()

            R.string.common_error.str(this@MyCardActivity)
                .alert(R.string.shuffle_call_word_error.str(this@MyCardActivity) + "\n" + t?.message,
                    this@MyCardActivity)
        }

        override fun onResponse(call: Call<List<HangrimWord>>, response: Response<List<HangrimWord>>) {
            val body = response.body()
            mycard_progress.hideProgressBar()

            if(body != null) {
                resetAdapter(body)
            } else {
                onFailure(null, null)
            }
        }
    }

    inner class GirdAdapter(val context: Context, val words: List<HangrimWord>) : BaseAdapter() {
        override fun getView(pos: Int, convertView: View?, root: ViewGroup?): View {
            val v = if(convertView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                inflater.inflate(R.layout.mycard_grid_item, root, false)
            } else {
                convertView
            }

            val word = words[pos]
            val imgView = v.findViewById<ImageView>(R.id.mycard_grid_image)
            val titleView = v.findViewById<TextView>(R.id.mycard_grid_title)
            val drawingFile = HGFunctions.isExsitDrawingFile(word, context)

            if (drawingFile != null) {
                // 드로잉한 그림 입니다.
                Thread {
                    val bitmap = BitmapFactory.decodeFile(drawingFile.absolutePath)
                    runOnUiThread { imgView.setImageBitmap(bitmap) }
                }.start()

                titleView.text = word.word_english
            } else {
                // 드로잉 하지 않은 그림 임.
                titleView.text = word.word_english
                imgView.setImageResource(R.drawable.ic_cardmenu_not_draw)
            }

            return v
        }

        override fun getItem(pos: Int) = words[pos]

        override fun getItemId(p0: Int) = p0.toLong()

        override fun getCount() = words.size
    }

    inner class MyExpressGridAdapter(val context: Context, val words: List<MyExpressWord>) : BaseAdapter() {
        override fun getView(pos: Int, convertView: View?, root: ViewGroup?): View {
            val v = if(convertView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                inflater.inflate(R.layout.mycard_grid_item, root, false)
            } else {
                convertView
            }

            val word = words[pos]
            val imgView = v.findViewById<ImageView>(R.id.mycard_grid_image)
            val titleView = v.findViewById<TextView>(R.id.mycard_grid_title)
            val drawingFile = File(context.filesDir, word.uuid)

            imgView.setImageBitmap(BitmapFactory.decodeFile(drawingFile.absolutePath))
            titleView.text = word.called

            return v
        }

        override fun getItem(pos: Int) = words[pos]

        override fun getItemId(p0: Int) = p0.toLong()

        override fun getCount() = words.size
    }

    /**
     * @Date 01.02 2019
     * OnNavMenuClickListen
     */
    inner class OnNavClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            if(v.id == R.id.mycard_cate_1) {
                // 장치에서 자신의 표현을 따로 가져와 업데이트 한다.
                val myExprs = SQLManager.getMyExpressWords(this@MyCardActivity)
                resetAdapterForExpress(myExprs.toList())
            } else {
                val category = when (v.id) {
                    R.id.mycard_cate_0 -> "Object"
                    R.id.mycard_cate_2 -> "Expression"
                    R.id.mycard_cate_3 -> "Kitchen"
                    R.id.mycard_cate_4 -> "People"
                    R.id.mycard_cate_5 -> "Time"
                    R.id.mycard_cate_6 -> "Number"
                    R.id.mycard_cate_7 -> "Body"
                    R.id.mycard_cate_8 -> "place"
                    R.id.mycard_cate_9 -> "Animal"
                    R.id.mycard_cate_10 -> "Etc"
                    else -> "Any"
                }

                requestWordFromServer(category)
            }

            onNavClickUpdate(v)
        }
    }
}