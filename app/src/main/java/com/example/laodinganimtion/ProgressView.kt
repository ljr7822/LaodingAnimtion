package com.example.laodinganimtion

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 * @author iwen大大怪
 * Create to 2020/9/26 8:53
 *
 */
class ProgressView: View {

    //定义变成半圆的动画因子
    private var cornerRadius = 0f

    //定义中间靠拢的动画因子
    private var tranx = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    // 顶部矩形画笔
    private val mRoundRectPaint = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
            tranx,
            0f,
            width-tranx,
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            mRoundRectPaint
        )
    }

    private fun startFinishAnim(){
        // 变成圆角矩形
        val toCircleAnim = ValueAnimator.ofFloat(0f, height/2f).apply {
            duration = 1000
            addUpdateListener {
                cornerRadius = it.animatedValue as Float
                invalidate()
            }
        }
        // 中间靠拢的动画
        val moveToCenterAnim = ValueAnimator.ofFloat(0f, (width-height)/2f).apply {
            duration = 2000
            addUpdateListener {
                tranx = it.animatedValue as Float
                invalidate()
            }
        }
        // 使用动画集，顺序播放动画
        AnimatorSet().apply {
            playSequentially(toCircleAnim, moveToCenterAnim)
            start()
        }
    }

    // 点击视图
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            startFinishAnim()
        }
        return true
    }
}