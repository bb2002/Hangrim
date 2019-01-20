package kr.saintdev.hangrim.views.activities.list

import android.content.Context
import android.content.DialogInterface
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
import android.view.View.*
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
import kr.saintdev.hangrim.views.activities.preview.DrawingPreviewActivity
import kr.saintdev.hangrim.views.activities.preview.MyExprViewActivity
import kr.saintdev.hangrim.views.adapter.HangrimWordAdapter
import kr.saintdev.hangrim.views.adapter.MyExpressAdapter
import kr.saintdev.hangrim.views.adapter.OnCardClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyCardActivity : AppCompatActivity(), OnCardClickListener {
    private lateinit var categoryButtons: Array<View>           // Category IMAGE VIEW
    private lateinit var categoryButtonText: Array<TextView>    // Category TEXT VIEW

    private lateinit var hangrimAdapter: HangrimWordAdapter     // Data Adapter [Server Data ONLY]
    private lateinit var myExpressAdapter: MyExpressAdapter     // Data Adapter [MyExpress ONLY]
    private var dataSetAdapter = CategoryItem.Source.SERVER     // Data Source

    private var autoScrollIdx = -1                             // Auto scroll position
    private var autoWordUUIDtoScrollIdx: String? = null        // When uuid -> scroll pos
    private var selectedCategory = CategoryItem.ID.OBJECT      // Selected category

    /**
     * @Date 01.20 2019
     * Override Activity functions
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        initXMLResources()

        // Set Auto scroll
        this.autoScrollIdx = intent.getIntExtra("scroll", -1)
        this.selectedCategory = CategoryItem.toCategoryID(intent.getStringExtra("category") ?: "Object")
        val isUseWordUUID = intent.getStringExtra("uuid") ?: null
        if(isUseWordUUID != null && this.autoScrollIdx == -1) {
            // Auto scroll idx
            this.autoWordUUIDtoScrollIdx = isUseWordUUID
        }

        // Set listener
        setSupportActionBar(mycard_toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mycard_go_home.setOnClickListener { finish() }

        // Set Grid view
        my_card_grid.setHasFixedSize(true)
        this.hangrimAdapter = HangrimWordAdapter(listOf(), this)
        this.myExpressAdapter = MyExpressAdapter(listOf(), this)
    }

    override fun onResume() {
        super.onResume()
        refreshDataSet()            // Refresh data set
    }

    override fun onClick(view: View, pos: Int) {
        when(this.dataSetAdapter) {
            CategoryItem.Source.SERVER -> {
                // ON SERVER
                val item = this.hangrimAdapter.dataset[pos]
                val file = HGFunctions.getSaveFileLocation("${item.prop_uuid}.png", this)

                if(file.exists()) {
                    val intent = Intent(this, DrawingPreviewActivity::class.java)
                    intent.putExtra("image", file.absolutePath)
                    intent.putExtra("word-english", item.word_english)
                    intent.putExtra("word-symbol", item.word_symbol)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, ShuffleActivity::class.java)
                    intent.putExtra("word-english", item.word_english)
                    intent.putExtra("word-korean", item.word_korean)
                    intent.putExtra("word-uuid", item.prop_uuid)
                    intent.putExtra("word-symbol", item.word_symbol)
                    intent.putExtra("word-category", item.prop_category)
                    startActivity(intent)

                    finish()
                }
            }

            CategoryItem.Source.DB -> {
                // ON DB
                val item = this.myExpressAdapter.dataset[pos]
                val intent = Intent(this, MyExprViewActivity::class.java)
                intent.putExtra("image", item.imagePath)
                startActivity(intent)
            }
        }

        this.autoScrollIdx = pos
    }

    override fun onLongClick(view: View, pos: Int) {
        super.onLongClick(view, pos)

        when(this.dataSetAdapter) {
            CategoryItem.Source.SERVER -> {
                val item = hangrimAdapter.dataset[pos]

                if(HGFunctions.getSaveFileLocation("${item.prop_uuid}.png", this).exists()) {
                    R.string.my_cards_remove.alert(
                        R.string.my_cards_remove_content,
                        this,
                        DialogInterface.OnClickListener { dialog, _ ->
                            SQLManager.removeShuffleWord(item.prop_uuid, this)
                            dialog.dismiss()
                            refreshDataSet()
                        }
                    )
                }
            }

            CategoryItem.Source.DB -> {
                val item = myExpressAdapter.dataset[pos]

                R.string.my_cards_remove.alert(R.string.my_cards_remove_content,this,
                    DialogInterface.OnClickListener { dialog, _ ->
                        SQLManager.removeMyExpressWord(item.uuid, this)
                        dialog.dismiss()
                        refreshDataSet()
                    }
                )
            }
        }
    }

    /**
     * @Date 01.20 2019
     * INIT Inner functions
     */
    private fun initXMLResources() {
        // INIT Category Buttons
        this.categoryButtons = arrayOf(
            mycard_cate_0, mycard_cate_1, mycard_cate_2,
            mycard_cate_3, mycard_cate_4, mycard_cate_5,
            mycard_cate_6, mycard_cate_7, mycard_cate_8,
            mycard_cate_9, mycard_cate_10 )

        // INIT Category Button Text
        this.categoryButtonText = arrayOf(
            mycard_cate_0_title, mycard_cate_1_title, mycard_cate_2_title,
            mycard_cate_3_title, mycard_cate_4_title, mycard_cate_5_title,
            mycard_cate_6_title, mycard_cate_7_title, mycard_cate_8_title,
            mycard_cate_9_title, mycard_cate_10_title )

        // Set Listener
        val listener = OnCategoryClickListener()
        for(btn in this.categoryButtons) btn.setOnClickListener(listener)
    }

    private fun setCategorySelection(category: CategoryItem.ID) {
        if(category == CategoryItem.ID.MY_OWN) {
            // Select MY Own
            applyButtonView(CategoryItem.ID.MY_OWN)
            val dataSet = SQLManager.getMyExpressWords(this@MyCardActivity)
            applyDataGridView(CategoryItem.Source.DB, dbData = dataSet)

            moveScrollToPosition()
        } else {
            // Select from server
            requestWordFromServer(category)
        }

        applyButtonView(category)           // Apply button view
        this.selectedCategory = category
    }

    private fun applyButtonView(category: CategoryItem.ID) {
        this.categoryButtonText.forEach { it.visibility = INVISIBLE }
        this.categoryButtonText[category.id].visibility = VISIBLE
    }

    private fun applyDataGridView(source: CategoryItem.Source,
                                  serverData: List<HangrimWord> = listOf(), dbData: List<MyExpressWord> = listOf()) {
        when(source) {
            CategoryItem.Source.SERVER -> {
                if(serverData.isEmpty()) {
                    // Set Empty set.
                    my_card_empty.visibility = VISIBLE
                    my_card_grid.visibility = INVISIBLE
                } else {
                    // Apply data
                    my_card_empty.visibility = GONE
                    my_card_grid.visibility = VISIBLE
                    this.hangrimAdapter = HangrimWordAdapter(serverData, this)
                    my_card_grid.adapter = this.hangrimAdapter
                }

                this.dataSetAdapter = CategoryItem.Source.SERVER
            }

            CategoryItem.Source.DB -> {
                if(dbData.isEmpty()) {
                    // Set Empty set.
                    my_card_empty.visibility = VISIBLE
                    my_card_grid.visibility = INVISIBLE
                } else {
                    // Apply data
                    my_card_empty.visibility = GONE
                    my_card_grid.visibility = VISIBLE
                    this.myExpressAdapter = MyExpressAdapter(dbData, this)
                    my_card_grid.adapter = this.myExpressAdapter
                }

                this.dataSetAdapter = CategoryItem.Source.DB
            }
        }
    }

    private fun requestWordFromServer(category: CategoryItem.ID) {
        val service = Retrofit.getRetrofit().create(HangrimService::class.java)
        val request = service.requestAllWords(category.key)
        request.enqueue(OnServerCallback())
        mycard_progress.showProgressBar()
    }

    private fun moveScrollToPosition() {
        if(this.autoScrollIdx != -1) {
            my_card_grid.scrollToPosition(this.autoScrollIdx)
            this.autoScrollIdx = -1
        } else if(this.autoWordUUIDtoScrollIdx != null) {
            // Use late auto scroll
            when(this.dataSetAdapter) {
                CategoryItem.Source.SERVER -> {
                    // Init auto scroll idx
                    val dataSet = this.hangrimAdapter.dataset
                    for(i in 0 until dataSet.size) {
                        val data = dataSet[i]
                        if(data.prop_uuid == this.autoWordUUIDtoScrollIdx)
                            this.autoScrollIdx = i
                    }
                }

                CategoryItem.Source.DB -> {
                    val dataSet = this.myExpressAdapter.dataset
                    for(i in 0 until dataSet.size) {
                        val data = dataSet[i]
                        if(data.uuid == this.autoWordUUIDtoScrollIdx)
                            this.autoScrollIdx = i
                    }
                }
            }

            this.autoWordUUIDtoScrollIdx = null
            moveScrollToPosition()
        }
    }

    private fun refreshDataSet() {
        setCategorySelection(selectedCategory)
    }

    /**
     * @Date 01.20 2019
     * Define inner class
     */
    inner class OnCategoryClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            var idx = 0
            for(i in 0 until categoryButtons.size) {
                if(categoryButtons[i].id == v.id) {
                    idx = i
                    break
                }
            }
            val category = CategoryItem.toCategoryID(idx)
            setCategorySelection(category)
        }
    }

    inner class OnServerCallback : Callback<List<HangrimWord>> {
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
                applyDataGridView(CategoryItem.Source.SERVER, body)
            } else {
                onFailure(null, null)
            }

            moveScrollToPosition()
        }
    }
}