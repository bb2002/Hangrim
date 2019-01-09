package kr.saintdev.hangrim.views.activities.drawing

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kr.saintdev.hangrim.R
import kr.saintdev.hangrim.views.fragments.hangrim.DrawPictureFragment
import kr.saintdev.hangrim.views.fragments.hangrim.ShareFragment
import kr.saintdev.hangrim.views.fragments.hangrim.WriteWordFragment

class CreateCardActivity : AppCompatActivity() {
    val FRAGMENTS = arrayOf(
        WriteWordFragment(),
        DrawPictureFragment(),
        ShareFragment()
    )
    val fragmentTemp = mutableMapOf<String, Any?>()           // 프래그먼트의 임시 저장 공간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        gotoForward()
    }

    var idx = 0

    /**
     * @Date 01.09 2019
     * 프레그먼트를 뒤로 이동
     */
    fun gotoBackward() : Boolean {
        return if (idx > 0) {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.hg_create_container, FRAGMENTS[--idx])
            trans.commit()
            true
        } else {
            false
        }
    }

    /**
     * @Date 01.09 2019
     * 프레그먼트를 앞으로 이동
     */
    fun gotoForward() : Boolean {
        return if (idx < FRAGMENTS.size) {
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.hg_create_container, FRAGMENTS[idx++])
            trans.commit()
            true
        } else {
            false
        }
    }
}