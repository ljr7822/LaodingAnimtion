package com.example.laodinganimtion

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress.result = ProgressView.FAILUEF
        // 2. 设置回调函数（谁实现给谁）
        mProgress.callBack = {
            // 做下载任务--将数据返回
            downloadData()
        }
    }

    // 自定义下载
    private fun downloadData(){
        ValueAnimator.ofFloat(0f,1.0f).apply {
            duration = 2000
            addUpdateListener {
                (it.animatedValue as Float).also {value ->
                    mProgress.progress = value
                }
            }
            start()
        }
    }
}