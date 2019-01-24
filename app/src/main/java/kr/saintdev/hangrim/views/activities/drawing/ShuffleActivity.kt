package kr.saintdev.hangrim.views.activities.drawing

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
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
    val fragmentTemp = mutableMapOf<String, Any?>()           // 프래그먼트의 임시 저장 공간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hangrim_shuffle)

        if(intent.getStringExtra("word-uuid") != null) {
            // preload setting
            fragmentTemp["word-english"] = intent.getStringExtra("word-english")
            fragmentTemp["word-korean"] = intent.getStringExtra("word-korean")
            fragmentTemp["word-uuid"] = intent.getStringExtra("word-uuid")
            fragmentTemp["word-category"] = intent.getStringExtra("word-category")
            fragmentTemp["word-symbol"] = intent.getStringExtra("word-symbol")
            fragmentTemp["word-preload"] = true
        }

        fragmentTemp["need-open"] = intent.getBooleanExtra("need-open", true)

        // 화면을 이동 한다.
        gotoForward()
    }


    var idx = -1
    var nowFragment: Fragment? = null
    /**
     * 프레그먼트를 뒤로 이동
     */
    fun gotoBackward() : Boolean {
         return if (idx > 0) {
             val trans = supportFragmentManager.beginTransaction()

             // 이전 프레그먼트 제거
             if(this.nowFragment != null) trans.remove(this.nowFragment)

             // 새 프레그먼트 생성
             nowFragment = createFragment(--idx)
             trans.replace(R.id.hg_shuffle_main, nowFragment)
             trans.commit()
             true
        } else {
             false
        }
    }

    /**
     * Fragment 를 앞으로 이동
     */
    fun gotoForward() =
        if(idx < 3) {
            val trans = supportFragmentManager.beginTransaction()

            // 이전 프레그먼트 제거
            if(this.nowFragment != null) trans.remove(this.nowFragment)
            this.nowFragment = createFragment(++idx)        // 새 프레그먼트 생성

            trans.replace(R.id.hg_shuffle_main, this.nowFragment)
            trans.commit()
            true
        } else {
            false
        }

    private fun createFragment(idx: Int) = when(idx) {
            0 -> ShuffleFragment()
            1 -> DrawPictureFragment()
            2 -> ShareFragment()
            else -> ShuffleFragment()
        }


    /**
     * @Date 12.28 2018
     * 기본 toolbar 의 작업
     */
    fun setToolbarBackbutton(b: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(b)
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