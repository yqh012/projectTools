package com.yqh.base.view

import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.yqh.tv.screen.tools.adapter.R

fun View.clickAnim() {
    setOnKeyListener { view, _, keyEvent ->
        when (keyEvent.action) {
            KeyEvent.ACTION_DOWN -> {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.KEYCODE_NUMPAD_ENTER ->
                        (view.getTag(R.id.view_click_nowtime) as? Long)?.let {
                            if (System.currentTimeMillis() - it > 300) {
                                view.smallAnim()
                                view.setTag(R.id.view_click_nowtime, System.currentTimeMillis())
                            } else {
                                view.setTag(R.id.view_click_nowtime, System.currentTimeMillis())
                                return@setOnKeyListener true
                            }
                        } ?: run {
                            view.smallAnim()
                            view.setTag(R.id.view_click_nowtime, System.currentTimeMillis())
                        }
                }
                false
            }
            KeyEvent.ACTION_UP -> {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.KEYCODE_NUMPAD_ENTER ->
                        view.normal()
                }
                false
            }
            else -> {
                false
            }
        }
    }
}

fun View.smallAnim(ratio: Float = 0.95F, duration: Long = 100) {
    animate()
        .scaleX(ratio)
        .scaleY(ratio)
        .setInterpolator(AccelerateInterpolator())
        .setDuration(duration).start()
}

fun View.normal(duration: Long = 100) {
    animate()
        .scaleY(1f)
        .scaleX(1f)
        .setInterpolator(AccelerateInterpolator())
        .setDuration(duration)
        .start()
}