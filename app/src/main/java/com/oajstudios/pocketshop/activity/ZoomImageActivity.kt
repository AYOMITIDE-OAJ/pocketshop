package com.oajstudios.pocketshop.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.models.StoreProductModel
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.changeBackgroundColor
import com.oajstudios.pocketshop.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.activity_zoom_image.*
import kotlinx.android.synthetic.main.layout_itemimage.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ZoomImageActivity : AppBaseActivity() {
    private val mImages = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_image)
        val mModel = intent?.getSerializableExtra(Constants.KeyIntent.DATA) as StoreProductModel
        setToolbar(toolbar)
        title = getString(R.string.lbl_images)
        mAppBarColor()
        llMain.changeBackgroundColor()
        for (i in mModel.images!!.indices) {
            mModel.images!![i].src.let { it1 -> mImages.add(it1!!) }
        }
        val adapter1 = ImageAdapter(mImages)
        productViewPager.adapter = adapter1
        dots.attachViewPager(productViewPager)
        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
    }
}

class ImageAdapter(private var mImg: ArrayList<String>) : PagerAdapter() {

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_itemzoom, parent, false)
                .apply {
                    imgSlider.loadImageFromUrl(mImg[position])
                }
        parent.addView(view)
        return view
    }

    override fun isViewFromObject(v: View, `object`: Any): Boolean = v === `object` as View

    override fun getCount(): Int = mImg.size

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) =
        parent.removeView(`object` as View)

}