package com.example.laodinganimtion

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.log

/**
 * 长方形变圆角矩形，再回缩
 * @author iwen大大怪
 * Create to 2020/9/26 8:53
 *
 */
class ProgressView : View {
    /**
     * 高阶函数：
     */
    // 1. 定义函数类型:
    // 参数：就是要返回的数据类型
    // 返回值：是否需要对方给我返回结果
    var callBack:(() -> Unit)? = null
    // 2. 设置回调函数（谁实现给谁）
    // 3. 调用

    //定义变成半圆的动画因子
    private var cornerRadius = 0f

    //定义中间靠拢的动画因子
    private var tranx = 0f

    //定义进度的变化因子 0-1.0
    var progress = 0f
        set(value) {
            field = value
            //刷新
            invalidate()

            if (progress == 1.0f){
                startFinishAnim()
            }
        }
    // 保存下载结果
    var result = UNKNOW
    // 伴生对象
    companion object {
        val SUCCESS = 0
        val FAILUEF = 1
        val UNKNOW = 2
    }
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    // 顶部矩形画笔
    private val mRoundRectPaint = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }

    // 顶部矩形画笔
    private val mRoundRectPaint1 = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        // 绘制背景--原始进度
        if (progress < 1.0f){
            canvas?.drawRoundRect(tranx, 0f, width.toFloat(), height.toFloat(), 0f, 0f, mRoundRectPaint)
        }
        // 绘制前景--下载进度
        canvas?.drawRoundRect(tranx,0f,progress*width-tranx,height.toFloat(), cornerRadius, cornerRadius, mRoundRectPaint1)

        // 判断是否要结果
        if (tranx == (width-height)/2f){
            if (result == SUCCESS || result == UNKNOW){
                // 下载成功

            }else{
                // 下载失败

            }
        }
    }

    // 开始动画
    private fun startFinishAnim() {
        // 变成圆角矩形
        val toCircleAnim = ValueAnimator.ofFloat(0f, height / 2f).apply {
            duration = 1000
            addUpdateListener {
                cornerRadius = it.animatedValue as Float
                invalidate()
            }
        }
        // 中间靠拢的动画
        val moveToCenterAnim = ValueAnimator.ofFloat(0f, (width - height) / 2f).apply {
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
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // 3. 调用
            callBack?.let {
                it()
            }
        }
        return true
    }
}