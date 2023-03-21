package com.oajstudios.pocketshop.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.*
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.oajstudios.pocketshop.R
import com.google.android.material.snackbar.Snackbar
import com.oajstudios.pocketshop.WooBoxApp.Companion.getAppInstance
import kotlin.math.roundToInt


fun View.snackBarError(msg: String) {
    val snackBar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    val sbView = snackBar.view
    sbView.setBackgroundColor(ContextCompat.getColor(getAppInstance(),R.color.tomato));snackBar.setTextColor(Color.WHITE);snackBar.show()
}

fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun View.fadeIn(duration: Int = 400) {
    clearAnimation()
    val alphaAnimation = AlphaAnimation(this.alpha, 1.0f)
    alphaAnimation.duration = duration.toLong()
    startAnimation(alphaAnimation)
}


val View.res: Resources get() = resources

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.changeBackgroundTint(color: Int) {
    (background as GradientDrawable).setColor(color)
    (background as GradientDrawable).setStroke(0, 0)
    background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
}

fun View.setStrokedBackground(backgroundColor: Int, strokeColor: Int = 0, alpha: Float = 1.0f, strokeWidth: Int = 3) {
    val drawable = background as GradientDrawable
    drawable.setStroke(strokeWidth, strokeColor)
    drawable.setColor(adjustAlpha(backgroundColor, alpha))
}
fun Context.color(color: Int): Int = ContextCompat.getColor(this, color)

fun adjustAlpha(color: Int, factor: Float): Int {
    val alpha = (Color.alpha(color) * factor).roundToInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

fun Context.getDisplayWidth(): Int = resources.displayMetrics.widthPixels
