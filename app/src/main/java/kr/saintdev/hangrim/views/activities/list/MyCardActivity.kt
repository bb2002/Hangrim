package kr.saintdev.hangrim.views.activities.list

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import co.ceryle.fitgridview.FitGridAdapter
import kotlinx.android.synthetic.main.activity_my_card.*
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.libs.func.HGFunctions
import kr.saintdev.hangrim.libs.func.alert
import kr.saintdev.hangrim.libs.func.str
import kr.saintdev.hangrim.modules.retrofit.HangrimService
import kr.saintdev.hangrim.modules.retrofit.HangrimWord
import kr.saintdev.hangrim.modules.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        // 서버로 부터 해당 카테고리의 단어를 가져온다.
        requestWordFromServer("Any")

        // set listener
        my_card_grid.onItemClickListener =  AdapterView.OnItemClickListener {
            parent, v, pos, id ->
            Toast.makeText(this@MyCardActivity, "$pos 클리!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @Date 01.01 2019
     * Request from server.
     */
    private fun requestWordFromServer(category: String) {
        val service = Retrofit.getRetrofit().create(HangrimService::class.java)
        val request = service.requestAllWords(category)
        request.enqueue(ServerCallBack())
    }

    /**
     * @Date 01.01 2019
     * Reset Adapter
     */
    private fun resetAdapter(words: List<HangrimWord>) {
        val adapter = GirdAdapter(applicationContext, words)
        my_card_grid.setFitGridAdapter(adapter)
        my_card_grid.update()
    }



    /**
     * @Date 01.01 2019
     * Callback from server.
     */
    inner class ServerCallBack : Callback<List<HangrimWord>> {
        override fun onFailure(call: Call<List<HangrimWord>>?, t: Throwable?) {

            R.string.common_error.str(this@MyCardActivity)
                .alert(R.string.shuffle_call_word_error.str(this@MyCardActivity) + "\n" + t?.message,
                    this@MyCardActivity)
        }

        override fun onResponse(call: Call<List<HangrimWord>>, response: Response<List<HangrimWord>>) {
            val body = response.body()

            if(body != null) {
                resetAdapter(body)
            } else {
                onFailure(null, null)
            }
        }
    }
}

class GirdAdapter(val context: Context, val words: List<HangrimWord>) : FitGridAdapter(context, R.layout.mycard_grid_item, words.size) {
    override fun onBindView(position: Int, view: View) {
        val word = words[position]
        val imgView = view.findViewById<ImageView>(R.id.mycard_grid_image)
        val titleView = view.findViewById<TextView>(R.id.mycard_grid_title)
        val drawingFile = HGFunctions.isExsitDrawingFile(word, context)

        if(drawingFile != null) {
            // 드로잉한 그림 입니다.
            imgView.setImageBitmap(BitmapFactory.decodeFile(drawingFile.absolutePath))
            titleView.text = word.word_english
        } else {
            // 드로잉 하지 않은 그림 임.
            titleView.text = word.word_english
        }
    }
}