package com.oajstudios.pocketshop.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.oajstudios.pocketshop.R

class CircleView : View {
    private var mRadius = DEFAULT_RADIUS.toFloat()
    private var paint: Paint? = null
    private var mX: Float = 0.toFloat()
    private var mCircleColor: Int = 0
    private var mGravity = 0

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initCircle(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initCircle(context, attrs)
    }

    constructor(context: Context) : super(context) {}

    private fun initCircle(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0)
        try {
            mCircleColor = ta.getColor(R.styleable.CircleView_color, DEFAULT_TEXT_COLOR)
            mRadius = ta.getDimension(R.styleable.CircleView_radius, DEFAULT_RADIUS.toFloat())
            mGravity = ta.getInteger(R.styleable.CircleView_circle_gravity, 0)
            paint = Paint()
            paint!!.style = Paint.Style.FILL
            paint!!.isAntiAlias = true
            paint!!.color = mCircleColor
        } finally {
            ta.recycle()
        }
    }

    /**
     * Get the x value of the pin
     *
     * @return x float value of the pin
     */
    override fun getX(): Float {
        return mX
    }

    /**
     * Set the x value of the pin
     *
     * @param x set x value of the pin
     */
    override fun setX(x: Float) {
        mX = x
    }

    fun setRadius(mRadius: Float) {
        this.mRadius = mRadius
        invalidate()
    }

    fun setCircleColor(mCircleColor: Int) {
        this.mCircleColor = mCircleColor
        paint!!.color = mCircleColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mGravity == 0) {
            canvas.drawCircle((width / 2).toFloat(), height / 2 - (height / 2 - mRadius), mRadius, paint!!)
        } else {
            canvas.drawCircle((width / 2).toFloat(), height / 2 + (height / 2 - mRadius), mRadius, paint!!)

        }

    }

    companion object {
        private val DEFAULT_TEXT_COLOR = Color.GREEN
        private val DEFAULT_RADIUS = 15
    }
}