package com.github.ihsg.patternlocker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class PatternIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "PatternIndicatorView"
    }

    var linkedLineView: IIndicatorLinkedLineView? = null
    var normalCellView: INormalCellView? = null
    var hitCellView: IHitCellView? = null
    var isShowLinkedLine = false    //是否显示连接线

    private var isError: Boolean = false
    private val hitIndexList: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }
    private val cellBeanList: List<CellBean> by lazy {
        val w = this.width - this.paddingLeft - this.paddingRight
        val h = this.height - this.paddingTop - this.paddingBottom
        CellFactory(w, h).cellBeanList
    }

    init {
        init(context, attrs, defStyleAttr)
    }

    fun updateState(hitIndexList: List<Int>?, isError: Boolean) {
        //1. clear pre state
        if (this.hitIndexList.isNotEmpty()) {
            this.hitIndexList.clear()
        }

        //2. record new state
        hitIndexList?.let {
            this.hitIndexList.addAll(it)
        }

        //3. update result
        this.isError = isError

        //4. update view
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        this.updateHitState()
        if (isShowLinkedLine) {
            this.drawLinkedLine(canvas)
        }
        this.drawCells(canvas)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0)
        isShowLinkedLine = ta.getBoolean(R.styleable.PatternIndicatorView_piv_is_show_linked_line, false)
        val normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, DefaultConfig.defaultNormalColor)
        val fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, DefaultConfig.defaultFillColor)
        val hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, DefaultConfig.defaultHitColor)
        val errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, DefaultConfig.defaultErrorColor)
        val lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, DefaultConfig.getDefaultLineWidth(resources))

        ta.recycle()

        val decorator = DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth)
        this.normalCellView = DefaultIndicatorNormalCellView(decorator)
        this.hitCellView = DefaultIndicatorHitCellView(decorator)
        this.linkedLineView = DefaultIndicatorLinkedLineView(decorator)
    }

    private fun initData() {
        this.hitIndexList.clear()
    }

    private fun updateHitState() {
        //1. clear pre state
        this.cellBeanList.forEach {
            it.isHit = false
        }

        //2. update hit state
        this.hitIndexList.let { it ->
            if (it.isNotEmpty()) {
                it.forEach {
                    if (0 <= it && it < this.cellBeanList.size) {
                        this.cellBeanList[it].isHit = true
                    }
                }
            }
        }
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (this.hitIndexList.isNotEmpty()) {
            this.linkedLineView?.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        this.cellBeanList.forEach {
            if (it.isHit && this.hitCellView != null) {
                this.hitCellView?.draw(canvas, it, this.isError)
            } else {
                this.normalCellView?.draw(canvas, it)
            }
        }
    }
}