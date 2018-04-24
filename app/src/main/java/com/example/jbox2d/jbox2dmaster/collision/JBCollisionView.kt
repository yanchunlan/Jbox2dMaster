package com.example.jbox2d.jbox2dmaster.collision

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @Author:  Ycl
 * @Date:  2018-04-23 11:59
 * @Desc:   JBox2d 碰撞容器类
 */

class JBCollisionView : FrameLayout {
    private val mJBImpl:JBCollisionImpl
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        setWillNotDraw(false)
        mJBImpl = JBCollisionImpl(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mJBImpl.onSizeChanged(w, h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mJBImpl.onLayout(changed)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mJBImpl.onDraw()
    }

    fun onSensorChanged(x: Float, y: Float) {
        mJBImpl.onSensorChanged(x, y)
    }

    fun onRandomChanged() {
        mJBImpl.onRandomChanged()
    }
}
