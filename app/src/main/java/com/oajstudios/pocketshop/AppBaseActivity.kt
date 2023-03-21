package com.oajstudios.pocketshop

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.oajstudios.pocketshop.WooBoxApp.Companion.noInternetDialog
import com.oajstudios.pocketshop.activity.DashBoardActivity
import com.oajstudios.pocketshop.utils.Constants.SharedPref.MODE
import com.oajstudios.pocketshop.utils.Constants.THEME.DARK
import com.oajstudios.pocketshop.utils.SLocaleHelper
import com.oajstudios.pocketshop.utils.extensions.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.custom_dialog.*

open class AppBaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    private var themeApp: Int = 0
    var onNetworkRetry: (() -> Unit)? = null


    fun disableHardwareRendering(v: View) {
        v.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    fun setToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
        mToolbar.changeToolbarFont()
        mToolbar.navigationIcon!!.setColorFilter(Color.parseColor(getTextTitleColor()),
            PorterDuff.Mode.SRC_ATOP)
        mToolbar.setTitleTextColor(Color.parseColor(getTextTitleColor()))
    }

    fun setDetailToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
        mToolbar.changeToolbarFont()
        mToolbar.navigationIcon!!.setColorFilter(Color.parseColor(getAccentColor()),
            PorterDuff.Mode.SRC_ATOP)
        mToolbar.setTitleTextColor(Color.parseColor(getTextTitleColor()))
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        switchToDarkMode(WooBoxApp.appTheme == DARK)
        super.onCreate(savedInstanceState)
        WooBoxApp.mAppDataChanges()
        setStatusBarGradient(this)
        noInternetDialog = null

        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            progressDialog!!.setContentView(R.layout.custom_dialog)
            if (getPrimaryColor().isNotEmpty()) {
                progressDialog!!.tv_progress_msg.changePrimaryColor()
            }
        }
        themeApp = WooBoxApp.appTheme
        if (!isNetworkAvailable()) {
            openNetworkDialog {
                recreate();onNetworkRetry?.invoke()
            }
        }

    }
    private fun setStatusBarGradient(activity: Activity) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val window = activity.window
                var flags = activity.window.decorView.systemUiVisibility
                flags =
                    flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                activity.window.decorView.systemUiVisibility = flags
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (getPrimaryColor().isEmpty()) {
                    window.statusBarColor =
                        ContextCompat.getColor(this, R.color.colorToolBarBackground)
                } else {
                    when {
                        getSharedPrefInstance().getIntValue(MODE, 1) == 1 -> {
                            window.statusBarColor = color(R.color.colorPrimary)

                        }
                        else -> {
                            window.statusBarColor = Color.parseColor(getPrimaryColor())
                        }
                    }
                }
            }
            else -> {
                if (getPrimaryColor().isEmpty()) {
                    window.statusBarColor =
                        ContextCompat.getColor(this, R.color.colorToolBarBackground)
                } else {
                    when {
                        getSharedPrefInstance().getIntValue(MODE, 1) == 1 -> {
                            window.statusBarColor = color(R.color.colorPrimary)

                        }
                        else -> {
                            window.statusBarColor = Color.parseColor(getPrimaryColor())
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun mAppBarColor() {
        val actionBar: ActionBar? = supportActionBar
        val colorDrawable: ColorDrawable = when {
            getSharedPrefInstance().getIntValue(MODE, 1) == 1 -> {
                ColorDrawable(getColor(R.color.colorPrimary))
            }
            else -> {
                ColorDrawable(Color.parseColor(getPrimaryColor()))
            }
        }
        actionBar!!.setBackgroundDrawable(colorDrawable)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(SLocaleHelper.onAttach(newBase!!)))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showProgress(show: Boolean) {
        when {
            show -> {
                if (!isFinishing) {
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.show()
                }
            }
            else -> try {
                if (progressDialog!!.isShowing && !isFinishing) {
                    progressDialog!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        Log.d("onStart", "called")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val appTheme = WooBoxApp.appTheme
        if (themeApp != 0 && themeApp != appTheme) {
            launchActivityWithNewTask<DashBoardActivity>()
        }
    }

}
