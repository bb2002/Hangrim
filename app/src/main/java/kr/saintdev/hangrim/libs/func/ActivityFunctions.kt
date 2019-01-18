package kr.saintdev.hangrim.libs.func

import android.view.View

object ActivityFunctions {
    /**
     * @Date 12.31 2018
     * 프리뷰 모드를 활성화 한다.
     * @param editor editor / view view
     */
    fun enablePreviewMode(vararg views: View) {
        views[0].visibility = View.GONE
        views[1].visibility = View.GONE
        views[2].visibility = View.VISIBLE
        views[3].visibility = View.VISIBLE
    }
}