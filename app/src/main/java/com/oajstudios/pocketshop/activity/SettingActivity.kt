package com.oajstudios.pocketshop.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.WooBoxApp
import com.oajstudios.pocketshop.adapter.RecyclerViewAdapter
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.SharedPref.LANGUAGE
import com.oajstudios.pocketshop.utils.SLocaleHelper
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.dialog_launguage_selection.*
import kotlinx.android.synthetic.main.spinner_language.view.*
import kotlinx.android.synthetic.main.toolbar.*

var mIsLanguageUpdated = false

class SettingActivity : AppBaseActivity() {
    private lateinit var lan: String

    private var codes = arrayOf(
        "en",
        "hi",
        "fr",
        "es",
        "de",
        "in",
        "af",
        "pt",
        "tr",
        "ar",
        "vi"
    )
    private var mCountryImg = intArrayOf(
        R.drawable.us,
        R.drawable.india,
        R.drawable.france,
        R.drawable.spain,
        R.drawable.germany,
        R.drawable.indonesia,
        R.drawable.south_africa,
        R.drawable.portugal,
        R.drawable.turkey,
        R.drawable.ar,
        R.drawable.vietnam
    )

    private var mIsSelectedByUser = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = getString(R.string.title_setting)
        setToolbar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAppBarColor()
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        lan = getAppLanguage()
        val languages = resources.getStringArray(R.array.language)
        switchNightMode.isChecked = WooBoxApp.appTheme == Constants.THEME.DARK
        mIsLanguageUpdated = false

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_launguage_selection)
        val languageAdapter = RecyclerViewAdapter(R.layout.spinner_language,
            onBind = { view: View, _: String, i: Int ->
                view.ivLogo.setImageResource(mCountryImg[i])
                view.tvName.text = languages[i]
            })
        languageAdapter.onItemClick = { i: Int, _: View, _: String ->
            ivLanguage.loadImageFromDrawable(mCountryImg[i])
            tvLanguage.text = languages[i]
            dialog.dismiss()
            setNewLocale(codes[i])
        }
        dialog.listLanguage.apply {
            setVerticalLayout()
            adapter = languageAdapter
        }
        languageAdapter.addItems(languages.toCollection(ArrayList()))
        llLanguage.onClick {
            dialog.show()
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            WooBoxApp.getAppInstance().enableNotification(isChecked)
        }

        codes.forEachIndexed { i: Int, s: String ->
            if (lan == s) {
                ivLanguage.loadImageFromDrawable(mCountryImg[i])
                tvLanguage.text = languages[i]
            }
        }

        switchNightMode.setOnCheckedChangeListener { _, isChecked ->
            WooBoxApp.changeAppTheme(isChecked)
            switchToDarkMode(isChecked)
        }

        Handler().postDelayed({ mIsSelectedByUser = true }, 1000)

    }

    override fun onBackPressed() {
        if (mIsLanguageUpdated) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun setNewLocale(language: String) {
        mIsLanguageUpdated = true
        getSharedPrefInstance().setValue(LANGUAGE, language)
        SLocaleHelper.setLocale(this, language)
        recreate()
    }
}


