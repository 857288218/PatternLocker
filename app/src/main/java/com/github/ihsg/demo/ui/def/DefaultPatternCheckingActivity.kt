package com.github.ihsg.demo.ui.def

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


class DefaultPatternCheckingActivity : AppCompatActivity() {
    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_pattern_checking)

        patternLockerView.linkedLineView = null
        patternLockerView.hitCellView = null
        patternLockerView.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isError = !isPatternOk(hitIndexList)
                view.updateStatus(isError)
                patternIndicatorView.updateState(hitIndexList, isError)
                updateMsg()
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.textMsg.text = "绘制解锁图案"
        this.patternHelper = PatternHelper()
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForChecking(hitIndexList)
        return this.patternHelper!!.isOk
    }

    private fun updateMsg() {
        this.textMsg.text = this.patternHelper!!.message
        this.textMsg.setTextColor(if (this.patternHelper!!.isOk)
            ContextCompat.getColor(this, R.color.colorPrimary)
        else
            ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper!!.isFinish) {
            finish()
        }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, DefaultPatternCheckingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
