package kr.saintdev.hangrim.views.activities.list

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
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
import kr.saintdev.hangrim.views.activities.drawing.ShuffleActivity
import kr.saintdev.hangrim.views.activities.preview.MyExprViewActivity
import kr.saintdev.hangrim.views.adapter.HangrimWordAdapter
import kr.saintdev.hangrim.views.adapter.MyExpressAdapter
import kr.saintdev.hangrim.views.adapter.OnCardClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyCardActivity : AppCompatActivity(), OnCardClickListener {
    private lateinit var categoryButtons: Array<View>
    private lateinit var categoryButtonText: Array<TextView>

    private lateinit var hangrimAdapter: HangrimWordAdapter
    private lateinit var myExpressAdapter: MyExpressAdapter
    private var selectedAdapter = 0
    private var selectedCategory = "Object"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

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

        // Set recycle grid view
        my_card_grid.setHasFixedSize(true)
        this.hangrimAdapter = HangrimWordAdapter(listOf(), this)
        this.myExpressAdapter = MyExpressAdapter(listOf(), this)

        // 기본값 선택
        onNavClickUpdate(mycard_cate_0)
    }

    override fun onResume() {
        super.onResume()

        if (this.selectedCategory == "CustomCard") {
            // 장치에서 자신의 표현을 따로 가져와 업데이트 한다.
            val myExprs = SQLManager.getMyExpressWords(this@MyCardActivity)
            resetAdapterForExpress(myExprs.toList())
        } else {
            requestWordFromServer(this.selectedCategory!!)
        }
    }

    /**
     * @Date 01.13 2019
     * CardView Click listener
     */
    override fun onClick(view: View, pos: Int) {
        if(this.selectedAdapter == 0) {
            // On Server
            val item = this.hangrimAdapter.dataset[pos]
            val intent = Intent(this, ShuffleActivity::class.java)
            intent.putExtra("word-english", item.word_english)
            intent.putExtra("word-korean", item.word_korean)
            intent.putExtra("word-uuid", item.prop_uuid)
            intent.putExtra("word-symbol", item.word_symbol)
            startActivity(intent)
        } else {
            // On Express
            val item = this.myExpressAdapter.dataset[pos]
            val intent = Intent(this, MyExprViewActivity::class.java)
            intent.putExtra("image", item.imagePath)
            startActivity(intent)
        }
    }

    override fun onLongClick(view: View, pos: Int) {
        Toast.makeText(this, "$pos long click!", Toast.LENGTH_SHORT).show()
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
        this.selectedAdapter = 0

        if(words.isEmpty()) {
            my_card_grid.visibility = View.GONE
        } else {
            my_card_grid.visibility = View.VISIBLE
            my_card_grid.adapter = this.hangrimAdapter
            this.hangrimAdapter.dataset = words
        }
    }

    private fun resetAdapterForExpress(words: List<MyExpressWord>) {
        this.selectedAdapter = 1

        if(words.isEmpty()) {
            my_card_grid.visibility = View.GONE
        } else {
            my_card_grid.visibility = View.VISIBLE
            my_card_grid.adapter = this.myExpressAdapter
            this.myExpressAdapter.dataset = words
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
                selectedCategory = "CustomCard"
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
                selectedCategory = category     // 현재 선택된 카테고리 업데이트
            }

            onNavClickUpdate(v)
        }
    }
}