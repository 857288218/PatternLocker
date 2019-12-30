package com.giantthong.otcassetswallet.mvvm.activity.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt
import com.github.ihsg.demo.R
import com.github.ihsg.demo.TestApplication
import com.github.ihsg.patternlocker.CellBean
import com.github.ihsg.patternlocker.IHitCellView

class RippleLockerIndicatorHitCellView : IHitCellView {

    @ColorInt
    private var hitColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0

    private val paint: Paint = Paint()

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        this.paint.style = Paint.Style.FILL
    }

    @ColorInt
    fun getHitColor(): Int {
        return hitColor
    }

    fun setHitColor(@ColorInt hitColor: Int): RippleLockerIndicatorHitCellView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): RippleLockerIndicatorHitCellView {
        this.errorColor = errorColor
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        //外圈浅色的圆圈
        this.paint.color = if (isError) TestApplication.getContext().resources.getColor(R.color.color_ff684e_10) else TestApplication.getContext().resources.getColor(R.color.color_f0f1ff)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        //中心的深色圆圈
        this.paint.color = getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 1.7f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getHitColor()
    }
}