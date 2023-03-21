package com.oajstudios.pocketshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.utils.Constants.SharedPref.SHOW_SWIPE
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_walk_through.*
import kotlinx.android.synthetic.main.theme3_walk.view.*

class WalkThroughActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through)
        if (getSharedPrefInstance().getBooleanValue(SHOW_SWIPE)) {
            launchActivity<DashBoardActivity> {
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            finish()
        }

        makeTransaprant()
        val adapter = WalkAdapter()
        theme3Viewpager.adapter = adapter
        dots.attachViewPager(theme3Viewpager)
        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
        btnStatShopping.onClick {
            launchActivity<DashBoardActivity> {
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            getSharedPrefInstance().setValue(SHOW_SWIPE, true)
            finish()
        }
        changeColor()
    }

    internal inner class WalkAdapter : PagerAdapter() {

        private var mHeading =
            arrayOf(
                getString(R.string.lbl_signin_up),
                getString(R.string.lbl_product_quality),
                getString(R.string.lbl_make_delicious_dishes)
            )

        private var mSubHeading = arrayOf(
            getString(R.string.lbl_dummy_text1),
            getString(R.string.lbl_dummy_text2),
            getString(R.string.lbl_dummy_text3)
        )

        private val mImg = intArrayOf(
            R.drawable.ic_walk_1,
            R.drawable.ic_trends,
            R.drawable.ic_walk_3
        )

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.theme3_walk, container, false)
            view.imgWalk.setImageResource(mImg[position])
            view.tvHeading.text = mHeading[position]
            view.tvHeading.changeTextPrimaryColor()
            view.tvSubHeading.text = mSubHeading[position]
            view.tvSubHeading.changeTextSecondaryColor()
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return mImg.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }
    }

    private fun changeColor() {
        btnStatShopping.changeBackgroundTint(getPrimaryColor())
    }

}
