package com.example.laodinganimtion

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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
    // 高阶函数：
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
    // 绘制勾勾或者叉叉的路径
    private val markPath = Path();
    // 结果的size
    private var markSize = 0f
    // 中心点坐标
    private var cx = 0f
    private var cy = 0f
    //勾勾裁剪动画的动画因⼦
    private var clipSize = 0f

    // 提供外部设置背景颜色
    var bgColor:Int = Color.BLACK
    // 提供外部设置进度颜色
    var proGressColor:Int = Color.MAGENTA
    // 提供外部设置勾勾叉叉的颜色
    var paintColor:Int = Color.WHITE

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        markSize = height / 3f
        cx = width / 2f
        cy = height / 2f
    }

    // 绘制背景画笔
    private val mRoundRectPaint = Paint().apply {
        color = proGressColor
        style = Paint.Style.FILL
    }

    // 绘制前景画笔
    private val mRoundRectPaint1 = Paint().apply {
        color = bgColor
        style = Paint.Style.FILL
    }

    // 画勾的画笔
    private val mCorrectPaint = Paint().apply {
        color =  paintColor
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas?) {
        // 绘制背景--原始进度
        if (progress < 1.0f){
            canvas?.drawRoundRect(tranx, 0f, width.toFloat(), height.toFloat(), 0f, 0f, mRoundRectPaint)
        }
        // 绘制前景--下载进度
        canvas?.drawRoundRect(tranx,0f,progress*width-tranx,height.toFloat(), cornerRadius, cornerRadius, mRoundRectPaint1)

        // 判断是否要返回什么结果（下载成功/下载失败）
        if (tranx == (width-height)/2f){
            if (result == SUCCESS || result == UNKNOW){
                // 下载成功--画对勾
                markPath.apply {
                    moveTo(cx - markSize/2, cy)
                    lineTo(cx, cy+markSize/2)
                    lineTo(cx + markSize/2, cy-markSize/2)
                }
            }else{
                // 下载失败--画叉叉
                markPath.apply {
                    moveTo(cx - markSize / 2, cy - markSize / 2)
                    lineTo(cx + markSize / 2, cy + markSize / 2)
                    moveTo(cx - markSize / 2, cy + markSize / 2)
                    lineTo(cx + markSize / 2, cy - markSize / 2)
                }
            }
            canvas?.drawPath(markPath,mCorrectPaint)
            //绘制遮罩层
            canvas?.drawRect(
            cx-markSize/2-10+clipSize,cy-markSize/ 2-10,cx+markSize/2+10,cy+markSize/2+10,mRoundRectPaint1)
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
        // 裁剪动画
        val clipAnim = ValueAnimator.ofFloat(0f,markSize+20).apply{
            duration = 1000
            addUpdateListener {anim->
                clipSize = anim.animatedValue as Float
                invalidate()
            }
        }
        // 使用动画集，顺序播放动画
        AnimatorSet().apply {
            playSequentially(toCircleAnim, moveToCenterAnim,clipAnim)
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