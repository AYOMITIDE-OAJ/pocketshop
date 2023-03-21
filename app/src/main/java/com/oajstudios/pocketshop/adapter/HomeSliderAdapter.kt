package com.oajstudios.pocketshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.models.DashboardBanner
import com.oajstudios.pocketshop.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.item_slider.view.*

class HomeSliderAdapter(private var mImg: List<DashboardBanner>) : PagerAdapter() {
    var size: Int? = null
    private var mListener: OnClickListener? = null
    fun setListener(mListener: OnClickListener) {
        this.mListener = mListener
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)

        view.img.loadImageFromUrl(mImg[position].image)
        view.setOnClickListener {
            if (mListener != null) {
                mListener!!.onClick(position, mImg)
            }
        }
        parent.addView(view)
        return view
    }

    override fun isViewFromObject(v: View, `object`: Any): Boolean = v === `object` as View

    override fun getCount(): Int = mImg.size

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) =
        parent.removeView(`object` as View)

    interface OnClickListener {
        fun onClick(position: Int, mImg: List<DashboardBanner>)
    }
}
