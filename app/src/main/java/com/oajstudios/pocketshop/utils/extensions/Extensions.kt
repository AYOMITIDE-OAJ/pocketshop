package com.oajstudios.pocketshop.utils.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.oajstudios.pocketshop.WooBoxApp.Companion.getAppInstance
import com.oajstudios.pocketshop.R

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun isNetworkAvailable(): Boolean {
    val info = getAppInstance().getConnectivityManager().activeNetworkInfo
    return info != null && info.isConnected
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

fun Activity.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()

fun Activity.snackBar(msg: String, length: Int = Snackbar.LENGTH_SHORT) =
    Snackbar.make(findViewById(android.R.id.content), msg, length)
        .setTextColor(color(R.color.white)).show()

fun Fragment.snackBar(msg: String) = activity!!.snackBar(msg)

fun Activity.snackBarError(msg: String) {
    val snackBar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
    val sbView = snackBar.view
    sbView.setBackgroundColor(getAppInstance().resources.getColor(R.color.tomato));snackBar.setTextColor(
        Color.WHITE
    );snackBar.show()
}

fun Activity.noInternetSnackBar() {
    this.snackBarError(getAppInstance().getString(R.string.error_no_internet))
}

fun Activity.showPermissionAlert(view: View) {
    val snackBar = Snackbar.make(
        view,
        getString(R.string.error_permission_required),
        Snackbar.LENGTH_INDEFINITE
    )
    val sbView = snackBar.view
    snackBar.setAction("Enable") {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
        snackBar.dismiss()
    }
    sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.tomato));snackBar.setTextColor(
        Color.WHITE
    );snackBar.show()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) =
    supportFragmentManager.inTransaction { replace(frameId, fragment) }

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) =
    supportFragmentManager.inTransaction { add(frameId, fragment) }

fun AppCompatActivity.removeFragment(fragment: Fragment) =
    supportFragmentManager.inTransaction { remove(fragment) }

fun AppCompatActivity.showFragment(fragment: Fragment) = supportFragmentManager.inTransaction {
    setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
    show(fragment)
}

fun AppCompatActivity.hideFragment(fragment: Fragment) = supportFragmentManager.inTransaction {
    hide(fragment)
}

fun AppCompatActivity.makeTransaprant() {
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

fun runDelayed(delayMillis: Long, action: () -> Unit) =
    Handler().postDelayed(Runnable(action), delayMillis)

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}

infix fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun Activity.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, stringRes, duration).show()

fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun Toolbar.changeToolbarFont() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView && view.text == title) {
            view.typeface = view.context.fontBold()
            break
        }
    }
}

fun ImageView.applyColorFilter(color: Int) {
    drawable.let { DrawableCompat.wrap(it) }.let {
        DrawableCompat.setTint(it, color)
    }
}

fun TextView.applyStrike() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

inline fun <reified T : Any> Activity.launchActivityWithNewTask() {
    launchActivity<T> {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(getAppInstance())
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)


fun FragmentActivity.requestPermissions(
    permissions: Array<String>,
    onResult: (isGranted: Boolean) -> Unit
) {
    if (isPermissionGranted(permissions)) {
        onResult(true)
        return
    }
    val observer = PermissionObserver()
    observer.onResumeCallback = {
        lifecycle.removeObserver(observer)
        onResult(isPermissionGranted(permissions))
    }
    lifecycle.addObserver(observer)
    ActivityCompat.requestPermissions(this, permissions, 100)
}

private class PermissionObserver : LifecycleObserver {
    var onResumeCallback: (() -> Unit)? = null

}

internal fun Drawable.tint(@ColorInt color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}

fun RecyclerView.setVerticalLayout(aReverseLayout: Boolean = false) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, aReverseLayout)
}

fun RecyclerView.setHorizontalLayout(aReverseLayout: Boolean = false) {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, aReverseLayout)
}

fun FragmentActivity.color(color: Int): Int = ContextCompat.getColor(this, color)

fun FragmentActivity.stringColor(color: String): Int = Color.parseColor(color)

fun AppCompatActivity.switchToDarkMode(isDark: Boolean) {
    if (isDark) {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        // makeNormalStatusBar(color(R.color.colorScreenBackground))
    } else {
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        //  lightStatusBar(color(R.color.colorScreenBackground))
    }
}

fun TextView.changeAccentColor() {
    setTextColor(Color.parseColor(getAccentColor()))
}

fun TextView.changePrimaryColor() {
    setTextColor(Color.parseColor(getPrimaryColor()))
}

fun TextView.changePrimaryColorDark() {
    setTextColor(Color.parseColor(getPrimaryColorDark()))
}

fun TextView.changeTextPrimaryColor() {
    setTextColor(Color.parseColor(getTextPrimaryColor()))

}

fun TextView.changeTextSecondaryColor() {
    setTextColor(Color.parseColor(getTextSecondaryColor()))
}

fun TextView.changeTitleColor() {
    setTextColor(Color.parseColor(getTextTitleColor()))
}
fun TextView.changeBackgroundTint(color: String) {
    background.setTint(Color.parseColor(color))
}

fun ImageView.changeBackgroundImageTint(color: String) {
    colorFilter = PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
}

fun View.changeTint(color: String) {
    background.colorFilter =
        PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
}

fun View.changeBackgroundColor() {
    setBackgroundColor(Color.parseColor(getBackgroundColor()))
}

fun setTextViewDrawableColor(textView: TextView, colors: String) {

    for (drawable in textView.compoundDrawablesRelative) {
        if (drawable != null) {
            drawable.colorFilter = PorterDuffColorFilter(
                Color.parseColor(colors), PorterDuff.Mode.SRC_IN
            )
        }
    }

}
