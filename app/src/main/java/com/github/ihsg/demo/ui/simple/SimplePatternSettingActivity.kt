package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.ihsg.demo.R
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import kotlinx.android.synthetic.main.activity_default_pattern_checking.*

class SimplePatternSettingActivity : AppCompatActivity() {

    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_pattern_setting)

        this.patternLockerView!!.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isOk = isPatternOk(hitIndexList)
                view.updateStatus(!isOk)
                patternIndicatorView!!.updateState(hitIndexList, !isOk)
                updateMsg()
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.textMsg.text = "设置解锁图案"
        this.patternHelper = PatternHelper()
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForSetting(hitIndexList)
        return this.patternHelper!!.isOk
    }

    private fun updateMsg() {
        this.textMsg.text = this.patternHelper!!.message
        this.textMsg.setTextColor(if (this.patternHelper!!.isOk)
            ContextCompat.getColor(this, R.color.colorAccent)
        else
            ContextCompat.getColor(this, R.color.color_red))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper!!.isFinish) {
            finish()
        }
    }

    companion object {


        fun startAction(context: Context) {
            val intent = Intent(context, SimplePatternSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
