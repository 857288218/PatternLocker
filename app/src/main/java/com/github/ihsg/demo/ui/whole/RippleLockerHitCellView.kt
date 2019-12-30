package com.giantthong.otcassetswallet.mvvm.activity.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt
import com.github.ihsg.demo.R
import com.github.ihsg.demo.TestApplication

import com.github.ihsg.patternlocker.CellBean
import com.github.ihsg.patternlocker.IHitCellView

class RippleLockerHitCellView : IHitCellView {

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

    fun setHitColor(@ColorInt hitColor: Int): RippleLockerHitCellView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): RippleLockerHitCellView {
        this.errorColor = errorColor
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

//        this.paint.color = getColor(isError) and 0x14FFFFFF
        this.paint.color = if (isError) TestApplication.getContext().resources.getColor(R.color.color_ff684e_10) else TestApplication.getContext().resources.getColor(R.color.color_f0f1ff)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

//        this.paint.color = getColor(isError) and 0x43FFFFFF
//        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius * 2f / 3f, this.paint)

        //中心的深色圆圈
        this.paint.color = getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3.5f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getHitColor()
    }
}
