package com.oajstudios.pocketshop.activity


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.fragments.MyCartFragment
import com.oajstudios.pocketshop.utils.BroadcastReceiverExt
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.addFragment
import com.oajstudios.pocketshop.utils.extensions.changeBackgroundColor
import kotlinx.android.synthetic.main.activity_my_cart.*
import kotlinx.android.synthetic.main.toolbar.*

class MyCartActivity : AppBaseActivity() {

    private var myCartFragment: MyCartFragment = MyCartFragment()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        setToolbar(toolbar)
        title = getString(R.string.menu_my_cart)
        mAppBarColor()
        rlMain.changeBackgroundColor()
        BroadcastReceiverExt(this) {
            onAction(Constants.AppBroadcasts.CARTITEM_UPDATE) {
                myCartFragment.invalidateCartLayout()
            }
        }
        addFragment(myCartFragment, R.id.container)
    }

    override fun onResume() {
        myCartFragment.invalidateCartLayout()
        super.onResume()
    }
}
