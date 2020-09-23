package com.example.laodinganimtion

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 等待加载动画
 * @author iwen大大怪
 * Create to 2020/9/23 14:33
 *
 */
class LoadingAnim : View {

    // 代码创建
    constructor(context: Context) : super(context) {}

    // xml创建
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    // 通过变量保存宽高
    private var mWidth = 0
    private var mHeight = 0

    // 顶部矩形的动画因子
    private var rectTopAngle = 0

    // 圆角矩形动画因子
    private var rx = 0f
    private var ry = 0f

    // 定义一个变量保存进度条动画对象
    private var rectAnim: ValueAnimator? = null

    // 定义一个变量保存圆角进度动画对象
    private var rectFAnim: ValueAnimator? = null

    // 动画集
    private var animators = AnimatorSet()

    // 底部矩形画笔
    private val mPaintBot = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }

    // 顶部矩形画笔
    private val mPaintTop = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    // 顶部矩形画笔
    private val mPaintTopF = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    // 计算宽高
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (width >= height) {
            // 如果宽大于高
            mWidth = width - 20
            mHeight = height - 20
        } else {
            // 如果高大于宽
            mWidth = width
            mHeight = height / 2
        }
    }

    // 绘制
    override fun onDraw(canvas: Canvas?) {
        // 底部长方形
        var rectBot = Rect(10, 10, 10 + mWidth, 10 + mHeight)
        // 顶部长方形
        var rectTop = Rect(0, 10, rectTopAngle, 10 + mHeight)
        // 圆角矩形
        var rectTopF = RectF(10f, 10f,10f + mWidth, 10f + mHeight)

        // 绘制底部长方形
        //canvas?.drawRect(rectBot, mPaintBot)
        // 绘制顶部长方形
        canvas?.drawRect(rectTop, mPaintTop)
        canvas?.drawRoundRect(rectTopF,rx,ry,mPaintTopF)
    }

    /**
     * 创建动画方法
     */
    private fun createAnim() {
        // 判断嘴巴动画对象是否存在
        if (rectAnim == null) {
            // 不存在，创建嘴巴动画
            rectAnim = ValueAnimator.ofInt(0, mWidth+10).apply {
                duration = 5000
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    rectTopAngle = it.animatedValue as Int
                    invalidate()
                }
            }
        }
        if (rectFAnim == null){
            // 不存在，创建嘴巴动画
            rectFAnim = ValueAnimator.ofFloat(0f, 110f).apply {
                duration = 5000
                addUpdateListener {
                    rx = it.animatedValue as Float
                    ry = it.animatedValue as Float
                    invalidate()
                }
            }
        }
        animators.playTogether(rectAnim, rectFAnim)
    }



    /**
     * 提供给外部启动这个动画的接口
     */
    fun startAnim() {
        // 创建动画
        createAnim()
        // 判断动画状态
        if (animators.isPaused) {
            animators.resume()
        } else {
            animators.start()
        }
    }

    /**
     * 提供给外部关闭这个动画的接口
     */
    fun stopAnim() {
        animators.pause()
    }
}