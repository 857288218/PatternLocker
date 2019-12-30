package com.giantthong.otcassetswallet.mvvm.activity.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt
import com.github.ihsg.demo.R
import com.github.ihsg.demo.TestApplication
import com.github.ihsg.patternlocker.CellBean
import com.github.ihsg.patternlocker.INormalCellView

class RippleLockerIndicatorNormalCellView : INormalCellView {

    @ColorInt
    private var normalColor: Int = 0

    private val paint: Paint = Paint()

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        this.paint.style = Paint.Style.FILL
    }

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int): RippleLockerIndicatorNormalCellView {
        this.normalColor = normalColor
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean) {
        val saveCount = canvas.save()

        //外圈设置为白色
        this.paint.color = TestApplication.getContext().resources.getColor(R.color.colorWhite)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        //中心的深色圆圈
        this.paint.color = getColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 1.7f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(): Int {
        return getNormalColor()
    }
}