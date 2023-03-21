package com.oajstudios.pocketshop.activity

import android.os.Bundle
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import android.widget.FrameLayout
import com.oajstudios.pocketshop.fragments.SignInFragment
import com.oajstudios.pocketshop.fragments.SignUpFragment
import com.oajstudios.pocketshop.utils.extensions.*


class SignInUpActivity : AppBaseActivity() {

    private val mSignInFragment: SignInFragment = SignInFragment()
    private val mSignUpFragment: SignUpFragment = SignUpFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_up)

        /**
         * Load Default Fragment
         */
        loadSignInFragment()
    }

    fun loadSignUpFragment() {
        if (mSignUpFragment.isAdded) {
            replaceFragment(mSignUpFragment, R.id.fragmentContainer)
            findViewById<FrameLayout>(R.id.fragmentContainer).fadeIn(500)
        } else {
            addFragment(mSignUpFragment, R.id.fragmentContainer)
        }
    }

    fun loadSignInFragment() {
        if (mSignInFragment.isAdded) {
            replaceFragment(mSignInFragment, R.id.fragmentContainer)
            findViewById<FrameLayout>(R.id.fragmentContainer).fadeIn(500)
        } else {
            addFragment(mSignInFragment, R.id.fragmentContainer)
        }
    }

    override fun onBackPressed() {
        when {
            mSignUpFragment.isVisible -> {
                removeFragment(mSignUpFragment)
            }
            else -> super.onBackPressed()

        }
    }
}