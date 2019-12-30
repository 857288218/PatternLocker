package com.github.ihsg.patternlocker

import android.support.annotation.ColorInt

data class DefaultStyleDecorator(
        @ColorInt var normalColor: Int,
        @ColorInt var fillColor: Int,
        @ColorInt var hitColor: Int,
        @ColorInt var errorColor: Int,
        var lineWidth: Float
)