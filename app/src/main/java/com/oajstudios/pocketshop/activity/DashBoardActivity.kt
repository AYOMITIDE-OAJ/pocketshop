package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.fragments.HomeFragment1
import com.oajstudios.pocketshop.fragments.HomeFragment2
import com.oajstudios.pocketshop.fragments.ViewAllProductFragment
import com.oajstudios.pocketshop.fragments.WishListFragment
import com.oajstudios.pocketshop.utils.BroadcastReceiverExt
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.AppBroadcasts.CART_COUNT_CHANGE
import com.oajstudios.pocketshop.utils.Constants.AppBroadcasts.PROFILE_UPDATE
import com.oajstudios.pocketshop.utils.Constants.SharedPref.KEY_DASHBOARD
import com.oajstudios.pocketshop.utils.SLocaleHelper
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_sidebar.*
import kotlinx.android.synthetic.main.toolbar.*

class DashBoardActivity : AppBaseActivity() {

    private var count: String = ""
    private lateinit var mHomeFragment: Fragment
    private val mWishListFragment = WishListFragment()
    private val mViewAllProductFragment = ViewAllProductFragment()
    private var selectedFragment: Fragment? = null
    private var selectedDashboard: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        selectedDashboard = getSharedPrefInstance().getIntValue(KEY_DASHBOARD, 0)
        if (selectedDashboard == 0) {
            mHomeFragment = HomeFragment1()
        } else if (selectedDashboard == 1) {
            mHomeFragment = HomeFragment2()
        }
        setToolbar(toolbar)
        setUpDrawerToggle()
        setListener()
        mAppBarColor()
        changeColor()
        if (isLoggedIn()) {
            cartCount()
            setCartCountFromPref()
        }
        setUserInfo()
        tvHome.onClick {
            loadFragment(mHomeFragment)
            title = getBuilderDashboard().appBars!!.title
            closeDrawer()
        }
        BroadcastReceiverExt(this) {
            onAction(CART_COUNT_CHANGE) {
                setCartCountFromPref()
            }
            onAction(PROFILE_UPDATE) {
                setUserInfo()
            }
        }
        tvWishlist.onClick {
            if (!isLoggedIn()) {
                launchActivity<SignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            launchActivity<WishListActivity>()
        }
        tvCategories.onClick {
            closeDrawer()
            launchActivity<CategoryActivity> {}
        }
        tvCart.onClick {
            if (!isLoggedIn()) {
                launchActivity<SignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            launchActivity<MyCartActivity>()
        }

        tvSettings.onClick {
            if (!isLoggedIn()) {
                launchActivity<SettingActivity>(requestCode = Constants.RequestCode.SETTINGS)
                return@onClick
            }
            closeDrawer()
            launchActivity<SettingActivity>(requestCode = Constants.RequestCode.SETTINGS)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RequestCode.ACCOUNT) {
                loadWishlistFragment()
            } else if (requestCode == Constants.RequestCode.LOGIN) {
                setUserInfo()
            }
        } else if (requestCode == Constants.RequestCode.SETTINGS) {
            SLocaleHelper.setLocale(this, getAppLanguage())
            launchActivityWithNewTask<DashBoardActivity>()
        }
    }

    private fun setUserInfo() {
        txtDisplayName.text = getDisplayName()
        txtDisplayEmail.text = getEmail()
        civProfile.loadImageFromUrl(getUserProfile(), aPlaceHolderImage = R.drawable.ic_profile)
        when {
            isLoggedIn() -> {
                tvSignIn.text = getString(R.string.btn_sign_out)
                tvCart.show()
                tvWishlist.show()
                tvOrder.show()
                tvChangePwd.show()
                rlProfile.show()
            }
            else -> {
                tvSignIn.text = getString(R.string.lbl_sign_in_link)
                tvCart.hide()
                tvWishlist.hide()
                tvOrder.hide()
                tvChangePwd.hide()
                rlProfile.hide()
            }
        }

    }

    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(llLeftDrawer))
            drawerLayout.closeDrawer(llLeftDrawer)
    }

    private fun setUpDrawerToggle() {
        val toggle = object :
            ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            ) {
            private val scaleFactor = 4f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                if (getAppLanguage() == "ar") {
                    main.translationX = -slideX
                } else {
                    main.translationX = slideX
                }

                main.scaleX = 1 - slideOffset / scaleFactor
                main.scaleY = 1 - slideOffset / scaleFactor
            }
        }
        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.drawerElevation = 0f
        toggle.isDrawerIndicatorEnabled = false
        toolbar.setNavigationIcon(R.drawable.ic_drawer)
        toolbar.navigationIcon!!.setColorFilter(
            Color.parseColor(getTextTitleColor()),
            PorterDuff.Mode.SRC_ATOP
        )
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setListener() {
        loadHomeFragment()
        rlProfile.onClick {
            when {
                isLoggedIn() -> launchActivity<EditProfileActivity>()
                else -> launchActivity<SignInUpActivity>()
            }
            closeDrawer()
        }

        tvOrder.onClick {
            when {
                !isLoggedIn() -> launchActivity<SignInUpActivity>()
                else -> launchActivity<OrderActivity>()
            }
            closeDrawer()

        }
        tvAbout.onClick { launchActivity<AboutActivity>(); closeDrawer() }

        tvChangePwd.onClick {
            when {
                !isLoggedIn() -> launchActivity<SignInUpActivity>()
                else -> launchActivity<ChangePwdActivity>()
            }
            closeDrawer()
        }
        tvSignIn.onClick {
            when {
                isLoggedIn() -> {
                    val dialog = getAlertDialog(
                        getString(R.string.lbl_logout_confirmation),
                        onPositiveClick = { _, _ ->
                            clearLoginPref()
                            launchActivityWithNewTask<DashBoardActivity>()
                        },
                        onNegativeClick = { dialog, _ ->
                            dialog.dismiss()
                        })
                    dialog.show()
                    closeDrawer()
                }
                else -> launchActivity<SignInUpActivity>(Constants.RequestCode.LOGIN)
            }
            closeDrawer()
        }
    }

    private fun loadFragment(aFragment: Fragment) {
        if (selectedFragment != null) {
            if (selectedFragment == aFragment) return
            hideFragment(selectedFragment!!)
        }
        if (aFragment.isAdded) {
            showFragment(aFragment)
        } else {

            addFragment(aFragment, R.id.container)
        }
        selectedFragment = aFragment
    }

    private fun loadHomeFragment() {
        loadFragment(mHomeFragment)
        title = getString(R.string.home)
        if (mHomeFragment is HomeFragment1) {
            (mHomeFragment as HomeFragment1)
        } else if (mHomeFragment is HomeFragment2) {
            (mHomeFragment as HomeFragment2)
        }
    }

    fun loadWishlistFragment() {
        loadFragment(mWishListFragment)
        title = getString(R.string.lbl_wish_list)
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            !mHomeFragment.isVisible -> loadHomeFragment()
            mViewAllProductFragment.isVisible -> {
                loadHomeFragment()
            }

            else -> super.onBackPressed()
        }

    }

    private fun cartCount() {
        setCartCountFromPref()
        sendCartCountChangeBroadcast()
    }

    private fun setCartCountFromPref() {
        count = getCartCount()
        if (mHomeFragment is HomeFragment1) {
            (mHomeFragment as HomeFragment1)
            (mHomeFragment as HomeFragment1).setCartCount()
        } else if (mHomeFragment is HomeFragment2) {
            (mHomeFragment as HomeFragment2)
            (mHomeFragment as HomeFragment2).setCartCount()
        }
    }

    private fun changeColor() {
        txtDisplayName.changeTextPrimaryColor()
        txtDisplayEmail.changeTextPrimaryColor()
        tvHome.changeTextPrimaryColor()
        tvCart.changeTextPrimaryColor()
        tvWishlist.changeTextPrimaryColor()
        tvCategories.changeTextPrimaryColor()
        tvOrder.changeTextPrimaryColor()
        tvChangePwd.changeTextPrimaryColor()
        tvSignIn.changeTextPrimaryColor()
        tvAbout.changeTextPrimaryColor()
        setTextViewDrawableColor(tvHome, getTextPrimaryColor())
        setTextViewDrawableColor(tvCart, getTextPrimaryColor())
        setTextViewDrawableColor(tvWishlist, getTextPrimaryColor())
        setTextViewDrawableColor(tvCategories, getTextPrimaryColor())
        setTextViewDrawableColor(tvOrder, getTextPrimaryColor())
        setTextViewDrawableColor(tvChangePwd, getTextPrimaryColor())
        setTextViewDrawableColor(tvSignIn, getTextPrimaryColor())
        setTextViewDrawableColor(tvAbout, getTextPrimaryColor())
    }

    override fun onResume() {
        super.onResume()
        if (selectedDashboard != getSharedPrefInstance().getIntValue(KEY_DASHBOARD, 0)) {
            recreate()
        }
    }
}
