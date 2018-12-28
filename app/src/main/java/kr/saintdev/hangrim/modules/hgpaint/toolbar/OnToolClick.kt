package kr.saintdev.hangrim.modules.hgpaint.toolbar

import kr.saintdev.hangrim.modules.hgpaint.HGPaint

interface OnToolClick {
    fun onClick(tool: HGToolbarTool, hgPaint: HGPaint)
}

enum class HGToolbarTool {
    BACKWARD,
    FORWARD
}