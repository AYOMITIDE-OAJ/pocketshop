package com.oajstudios.pocketshop.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView

import androidx.annotation.RequiresApi

import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.utils.extensions.hide
import com.oajstudios.pocketshop.utils.extensions.show

import java.util.Random

class ExpandableTextView : FrameLayout {

    private var mMainLayout: View? = null
    var content: TextView? = null
    var moreLess: TextView? = null
    var maxLine = Integer.MAX_VALUE
    private var collapsedHeight: Int = 0
    private var expandInterpolator: TimeInterpolator? = null
    private var collapseInterpolator: TimeInterpolator? = null
    /**
     * Sets the duration of the expand / collapse animation.
     *
     * @param animationDuration duration in milliseconds.
     */
    var animationDuration: Long = 300

    var moreLessGravity: Int = 0
        set(moreLessGravity) {
            var i = Gravity.LEFT
            field = moreLessGravity
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END or Gravity.BOTTOM
            moreLess!!.layoutParams = params
        }
    /**
     * Options
     */
    var isMoreLessShow: Boolean = false
        set(moreLessShow) {
            field = moreLessShow
            if (moreLessShow) {
                moreLess!!.show()
            } else {
                moreLess!!.hide()
            }
        }
    private var moreLessTextStyle: Int = 0
    private var contentTextStyle: Int = 0
    var isDefaultExpand: Boolean = false
        private set
    private var mContext: Context? = null


    private fun getID(): Int {
        val generator = Random()
        return generator.nextInt(Integer.MAX_VALUE)

    }

    constructor(context: Context) : super(context) {
        this.mContext = context
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.mContext = context
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mMainLayout = View.inflate(context, R.layout.expandable_text_view_layout, this)
        content = findViewById<View>(R.id.content) as TextView
        moreLess = findViewById<View>(R.id.moreLess) as TextView
        content!!.setOnClickListener { toggle() }
        moreLess!!.setOnClickListener { toggle() }
        content!!.id = getID()
        // create default interpolators
        expandInterpolator = AccelerateDecelerateInterpolator()
        collapseInterpolator = AccelerateDecelerateInterpolator()
        applyXmlAttributes(attrs)
    }

    private fun applyXmlAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
            try {
                // maxLine = a.getInt(R.styleable.ExpandableTextView_hnc_maxLine, maxLine);
                maxLine = 3
                isDefaultExpand = true
                content!!.text = a.getString(R.styleable.ExpandableTextView_hnc_text)
                if (isDefaultExpand) {
                    content!!.maxLines = Integer.MAX_VALUE
                    moreLess!!.text = resources.getString(R.string.more)
                } else {
                    content!!.maxLines = maxLine
                    moreLess!!.text = resources.getString(R.string.less)
                }
                moreLess!!.isAllCaps = false
                isMoreLessShow = true
                moreLessGravity = a.getInt(R.styleable.ExpandableTextView_hnc_moreLessGravity, Gravity.LEFT)
                moreLessTextStyle = a.getInt(R.styleable.ExpandableTextView_hnc_moreLessTextStyle, Typeface.NORMAL)
                contentTextStyle = a.getInt(R.styleable.ExpandableTextView_hnc_TextStyle, Typeface.NORMAL)
                animationDuration = a.getInt(R.styleable.ExpandableTextView_hnc_animationDuration, 300).toLong()
            } finally {
                a.recycle()
            }
        }
    }

    fun toggle() {
        if (moreLess!!.text == resources.getString(R.string.more)) {
            toggle(true)
        } else {
            toggle(false)
        }
    }

    private fun toggle(expand: Boolean) {
        if (expand) {
            expand()
            moreLess!!.text = resources.getString(R.string.less)
            moreLess!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up,0)
            return
        }
        collapse()
        moreLess!!.text = resources.getString(R.string.more)
        moreLess!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down,0)

    }

    fun setText(text: String) {
        content!!.maxLines = Integer.MAX_VALUE
        content!!.text = text
        val vto = content!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                val obs = content!!.viewTreeObserver
                obs.removeOnGlobalLayoutListener(this)
                if (content!!.lineCount <= maxLine) {
                    moreLess!!.hide()
                } else {
                    moreLess!!.show()
                    content!!.maxLines = maxLine
                }
            }
        })
    }

    /**
     * Expand this [TextView].
     *
     * @return true if expanded, false otherwise.
     */
    fun expand() {

        // get collapsed height
        content!!.measure(
                View.MeasureSpec.makeMeasureSpec(content!!.measuredWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        collapsedHeight = content!!.measuredHeight

        // set maxLines to MAX Integer, so we can calculate the expanded height
        content!!.maxLines = Integer.MAX_VALUE

        // get expanded height
        content!!.measure(
                View.MeasureSpec.makeMeasureSpec(content!!.measuredWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val expandedHeight = content!!.measuredHeight

        // animate from collapsed height to expanded height
        val valueAnimator = ValueAnimator.ofInt(collapsedHeight, expandedHeight)
        valueAnimator.addUpdateListener { animation ->
            val layoutParams = content!!.layoutParams
            layoutParams.height = animation.animatedValue as Int+70

            content!!.layoutParams = layoutParams
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // if fully expanded, set height to WRAP_CONTENT, because when rotating the device
                // the height calculated with this ValueAnimator isn't correct anymore
                val layoutParams = content!!.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                (layoutParams as FrameLayout.LayoutParams).bottomMargin=70
                content!!.layoutParams = layoutParams
            }
        })

        // set interpolator
        valueAnimator.interpolator = expandInterpolator

        // start the animation
        valueAnimator
                .setDuration(animationDuration)
                .start()

    }

    /**
     * Collapse this [TextView].
     *
     * @return true if collapsed, false otherwise.
     */
    fun collapse() {
        // get expanded height
        val expandedHeight = content!!.measuredHeight

        // animate from expanded height to collapsed height
        val valueAnimator = ValueAnimator.ofInt(expandedHeight, collapsedHeight)
        valueAnimator.addUpdateListener { animation ->
            val layoutParams = content!!.layoutParams
            layoutParams.height = animation.animatedValue as Int-70
            content!!.layoutParams = layoutParams
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // set maxLines to original value
                content!!.maxLines = maxLine

                // if fully collapsed, set height to WRAP_CONTENT, because when rotating the device
                // the height calculated with this ValueAnimator isn't correct anymore
                val layoutParams = content!!.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                (layoutParams as FrameLayout.LayoutParams).bottomMargin=0
                content!!.layoutParams = layoutParams
            }
        })

        // set interpolator
        valueAnimator.interpolator = collapseInterpolator

        // start the animation
        valueAnimator
                .setDuration(animationDuration)
                .start()

    }




}