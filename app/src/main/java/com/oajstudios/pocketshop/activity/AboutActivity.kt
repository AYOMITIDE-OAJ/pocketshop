package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.utils.Constants.SharedPref.CONTACT
import com.oajstudios.pocketshop.utils.Constants.SharedPref.COPYRIGHT_TEXT
import com.oajstudios.pocketshop.utils.Constants.SharedPref.FACEBOOK
import com.oajstudios.pocketshop.utils.Constants.SharedPref.INSTAGRAM
import com.oajstudios.pocketshop.utils.Constants.SharedPref.PRIVACY_POLICY
import com.oajstudios.pocketshop.utils.Constants.SharedPref.TERM_CONDITION
import com.oajstudios.pocketshop.utils.Constants.SharedPref.TWITTER
import com.oajstudios.pocketshop.utils.Constants.SharedPref.WHATSAPP
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar.*

class AboutActivity : AppBaseActivity() {

    private var whatsUp: String = "https://wa.me/08082509646"
    private var instagram: String = ""
    private var twitter: String = "https://www.twitter.com/azionspecie"
    private var facebook: String = ""
    private var contact: String = ""
    private var copyRight: String = ""
    private var privacy: String = ""
    private var toc: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setToolbar(toolbar)
        title = getString(R.string.lbl_about)
        mAppBarColor()
        changeColor()
        getSharedPrefInstance().apply {
            whatsUp = getStringValue(WHATSAPP)
            instagram = getStringValue(INSTAGRAM)
            twitter = getStringValue(TWITTER)
            facebook = getStringValue(FACEBOOK)
            contact = getStringValue(CONTACT)
            copyRight = getStringValue(COPYRIGHT_TEXT)
            privacy = getStringValue(PRIVACY_POLICY)
            toc = getStringValue(TERM_CONDITION)
        }
        if (copyRight.isEmpty()) {
            tvCopyRight.hide()
        } else {
            tvCopyRight.text = copyRight
            tvCopyRight.show()
        }

        tvVersion.text = "Version: "

        if (whatsUp.isEmpty()) iv_whatsapp.hide()
        if (privacy.isEmpty()) tvPrivacyPolicy.hide()
        if (toc.isEmpty()) tvTOC.hide()
        if (instagram.isEmpty()) iv_instagram.hide()
        if (twitter.isEmpty()) iv_twitter_sign.hide()
        if (facebook.isEmpty()) iv_facebook.hide()
        if (contact.isEmpty()) iv_contact.hide()
        llBottom.show()

        iv_whatsapp.onClick { openCustomTab("https://wa.me/${whatsUp}") }
        iv_instagram.onClick { openCustomTab(instagram) }
        iv_twitter_sign.onClick { openCustomTab(twitter) }
        iv_facebook.onClick { openCustomTab(facebook) }
        iv_contact.onClick { dialNumber(contact) }
        tvPrivacyPolicy.onClick { openCustomTab(privacy) }
        tvTOC.onClick { openCustomTab(toc) }

        if (whatsUp.isEmpty() && instagram.isEmpty() && twitter.isEmpty() && facebook.isEmpty() && contact.isEmpty()) {
            llFollow.hide()
        } else {
            llFollow.show()
        }
    }

    private fun changeColor() {
        tvAppName.changeTextPrimaryColor()
        tvVersion.changeTextSecondaryColor()
        tvCopyRight.changeTextSecondaryColor()
        tvTOC.changeTextSecondaryColor()
        tvPrivacyPolicy.changeTextSecondaryColor()
        llFollow.changeTextPrimaryColor()
        rlMain.changeBackgroundColor()
    }
}
