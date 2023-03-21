package com.oajstudios.pocketshop.activity


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.fragments.ViewAllProductFragment
import com.oajstudios.pocketshop.utils.BroadcastReceiverExt
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.AppBroadcasts.CART_COUNT_CHANGE
import com.oajstudios.pocketshop.utils.extensions.addFragment
import com.oajstudios.pocketshop.utils.extensions.getHtmlString
import kotlinx.android.synthetic.main.toolbar.*

class ViewAllProductActivity : AppBaseActivity() {

    private var mFragment: ViewAllProductFragment? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)
        setToolbar(toolbar)

        BroadcastReceiverExt(this) { onAction(CART_COUNT_CHANGE) { mFragment?.setCartCount() } }

        title = intent.getStringExtra(Constants.KeyIntent.TITLE)?.getHtmlString()
        mAppBarColor()
        val mViewAllId = intent.getIntExtra(Constants.KeyIntent.VIEWALLID, 0)
        val mCategoryId = intent.getIntExtra(Constants.KeyIntent.KEYID, -1)
        val specialProduct = intent.getStringExtra(Constants.KeyIntent.SPECIAL_PRODUCT_KEY)
        mFragment = if (specialProduct != null) {
            ViewAllProductFragment.getNewInstance(
                mViewAllId,
                mCategoryId,
                specialProduct = specialProduct
            )
        } else {
            ViewAllProductFragment.getNewInstance(mViewAllId, mCategoryId)
        }

        addFragment(mFragment!!, R.id.fragmentContainer)
    }
}