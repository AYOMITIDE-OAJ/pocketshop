package com.oajstudios.pocketshop.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.fragments.WishListFragment
import com.oajstudios.pocketshop.utils.extensions.addFragment
import kotlinx.android.synthetic.main.toolbar.*

class WishListActivity : AppBaseActivity() {

    private var wishListFragment: WishListFragment = WishListFragment()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        setToolbar(toolbar)
        title = getString(R.string.lbl_wish_list)
        mAppBarColor()
        addFragment(wishListFragment, R.id.container)
    }

    override fun onResume() {
        wishListFragment.wishListItemChange()
        super.onResume()
    }
}
