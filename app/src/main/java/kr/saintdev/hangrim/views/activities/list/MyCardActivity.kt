package kr.saintdev.hangrim.views.activities.list

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import co.ceryle.fitgridview.FitGridAdapter
import kotlinx.android.synthetic.main.activity_my_card.*
import kr.saintdev.hangrim.R

class MyCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        gridView.setFitGridAdapter(TestAdapter(this))
        gridView.update()
    }

    inner class TestAdapter(context: Context) : FitGridAdapter(context, R.layout.grid_test, 5) {
        override fun onBindView(position: Int, view: View) {
            view.findViewById<ImageView>(R.id.grid_image).setImageResource(R.drawable.ic_pentool_close)
        }
    }
}