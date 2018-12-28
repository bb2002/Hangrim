package kr.saintdev.hangrim.views.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.views.fragments.shuffle.DrawPictureFragment
import kr.saintdev.hangrim.views.fragments.shuffle.ShareFragment
import kr.saintdev.hangrim.views.fragments.shuffle.ShuffleFragment

/**
 * Date 12.19 2018
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * Hangrim Shuffle Activity.
 */
class ShuffleActivity : AppCompatActivity() {
    private val FRAGMENTS = arrayOf(
        ShuffleFragment(),
        DrawPictureFragment(),
        ShareFragment())
    private lateinit var toolbar: Toolbar
    val fragmentTemp = mutableMapOf<String, Any?>()           // 프래그먼트의 임시 저장 공간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hangrim_shuffle)

        // Toolbar setting
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(this.toolbar)

        // 화면을 이동 한다.
        gotoForward()
    }

    var idx = 0
    /**
     * 프레그먼트를 뒤로 이동
     */
    fun gotoBackward() =
        if(idx < 0) {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.hg_shuffle_main, FRAGMENTS[--idx])
            trans.commit()
            true
        } else {
            false
        }

    /**
     * Fragment 를 앞으로 이동
     */
    fun gotoForward() =
        if(idx < FRAGMENTS.size) {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.hg_shuffle_main, FRAGMENTS[idx++])
            trans.commit()
            true
        } else {
            false
        }


    /**
     * @Date 12.28 2018
     * 기본 toolbar 의 작업
     */
    fun setToolbarBackbutton(b: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(b)
    }

    fun setCustomToolbar(v: View) {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(
            v,
            ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                if(!gotoBackward()) finish()
            }
        }

        return true
    }
}