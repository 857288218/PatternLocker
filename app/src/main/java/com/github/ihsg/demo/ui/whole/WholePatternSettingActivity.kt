package com.github.ihsg.demo.ui.whole

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.giantthong.otcassetswallet.mvvm.activity.patternlocker.RippleLockerHitCellView
import com.giantthong.otcassetswallet.mvvm.activity.patternlocker.RippleLockerIndicatorHitCellView
import com.giantthong.otcassetswallet.mvvm.activity.patternlocker.RippleLockerIndicatorNormalCellView
import com.github.ihsg.demo.R
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.*
import kotlinx.android.synthetic.main.activity_simple_pattern_checking.patternIndicatorView
import kotlinx.android.synthetic.main.activity_simple_pattern_checking.patternLockerView
import kotlinx.android.synthetic.main.activity_simple_pattern_checking.textMsg
import kotlinx.android.synthetic.main.activity_whole_pattern_setting.*

class WholePatternSettingActivity : AppCompatActivity() {

    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whole_pattern_setting)

        val decorator = (this.patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator
        //绘制操作时的cell样式
        this.patternLockerView.hitCellView = RippleLockerHitCellView().setHitColor(decorator.hitColor).setErrorColor(decorator.errorColor)
        //绘制操作时的indicator样式
        val decoratorHitIndicator = (patternIndicatorView.hitCellView as DefaultIndicatorHitCellView).styleDecorator
        patternIndicatorView.hitCellView = RippleLockerIndicatorHitCellView().setHitColor(decoratorHitIndicator.hitColor).setErrorColor(decoratorHitIndicator.errorColor)
        //绘制无操作时的indicator样式
        val decoratorNormalIndicator = (patternIndicatorView.normalCellView as DefaultIndicatorNormalCellView).styleDecorator
        patternIndicatorView.normalCellView = RippleLockerIndicatorNormalCellView().setNormalColor(decoratorNormalIndicator.normalColor)

        this.patternLockerView!!.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isOk = isPatternOk(hitIndexList)
                view.updateStatus(!isOk)
                updateMsg()
                //只有第一次绘制成功才显示Indicator的绘制路线
                if (isOk && patternHelper?.isTwiceConfirmStatus == true) {
                    patternIndicatorView!!.updateState(hitIndexList, !isOk)
                }
                //二次绘制显示重设
                if (patternHelper?.isTwiceConfirmStatus == true) {
                    tv_reset.visibility = View.VISIBLE
                }
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.patternHelper = PatternHelper()

        findViewById<View>(R.id.btn_clean).setOnClickListener { patternLockerView!!.clearHitState() }
        tv_reset.setOnClickListener {
            tv_reset.visibility = View.INVISIBLE
            patternHelper?.resetPwd()
            updateMsg()
            patternIndicatorView!!.updateState(ArrayList(), false)
            patternLockerView.clearHitState()
        }
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForSetting(hitIndexList)
        return this.patternHelper!!.isOk
    }

    private fun updateMsg() {
        this.textMsg.text = this.patternHelper!!.message
        this.textMsg.setTextColor(if (this.patternHelper!!.isOk)
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        else
            ContextCompat.getColor(this, R.color.color_red))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper!!.isFinish) {
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT)
            finish()
        }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, WholePatternSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
