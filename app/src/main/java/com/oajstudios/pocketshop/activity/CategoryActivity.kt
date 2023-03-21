package com.oajstudios.pocketshop.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.Category
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.KEYID
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.TITLE
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.VIEWALLID
import com.oajstudios.pocketshop.utils.Constants.TotalItem.TOTAL_CATEGORY_PER_PAGE
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.item_viewproductgrid.view.*
import kotlinx.android.synthetic.main.toolbar.*

class CategoryActivity : AppBaseActivity() {
    private var showPagination: Boolean? = true
    private var mIsLoading = false
    private var countLoadMore = 1
    private val data: MutableMap<String, Int> =
        HashMap()
    private var isLastPage: Boolean? = false

    private val mProductAdapter =
        BaseAdapter<Category>(R.layout.item_viewcat, onBind = { view, model, _ ->

            if (model.image !== null) {
                view.ivProduct.loadImageFromUrl(model.image.src)
                view.ivProduct.show()
            } else {
                view.ivProduct.loadImageFromDrawable(R.drawable.app_logo)
            }
            view.tvProductName.text = model.name.getHtmlString()
           // view.tvProductName.changeTextPrimaryColor()
            view.onClick {
                launchActivity<ViewAllProductActivity> {
                    putExtra(TITLE, model.name)
                    putExtra(VIEWALLID, Constants.viewAllCode.CATEGORY)
                    putExtra(KEYID, model.id)
                }
            }
        })


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setToolbar(toolbar)
        title = getString(R.string.lbl_category)
        mAppBarColor()
        changeColor()
        data["page"] = countLoadMore
        data["per_page"] = TOTAL_CATEGORY_PER_PAGE
        data["parent"] = 0
        loadData(data)
        rvNewestProduct.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            setHasFixedSize(true)
            adapter = mProductAdapter
            rvItemAnimation()
            if (showPagination!!) {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val countItem = recyclerView.layoutManager?.itemCount

                        var lastVisiblePosition = 0
                        if (recyclerView.layoutManager is LinearLayoutManager) {
                            lastVisiblePosition =
                                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        } else if (recyclerView.layoutManager is GridLayoutManager) {
                            lastVisiblePosition =
                                (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                        }
                        if (isLastPage == false) {
                            if (lastVisiblePosition != 0 && !mIsLoading && countItem?.minus(1) == lastVisiblePosition) {
                                mIsLoading = true
                                countLoadMore = countLoadMore.plus(1)
                                data["page"] = countLoadMore
                                data["per_page"] = TOTAL_CATEGORY_PER_PAGE
                                data["parent"] = 0
                                loadData(data)
                            }
                        }
                    }
                })
            }
        }

    }

    private fun loadData(data: MutableMap<String, Int>) {
        if (isNetworkAvailable()) {
            showProgress(true)
            getRestApiImpl().listAllCategory(data, onApiSuccess = {
                showProgress(false)
                if (countLoadMore == 1) {
                    mProductAdapter.clearItems()
                }
                if (it.isEmpty()) {
                    isLastPage = true
                }
                mIsLoading = false
                mProductAdapter.addMoreItems(it)
                if (mProductAdapter.itemCount == 0) {
                    rvNewestProduct.hide()
                } else {
                    rvNewestProduct.show()
                }
            }, onApiError = {
                showProgress(false)
                snackBar(it)
            })

        }
    }

    private fun changeColor() {
        llMain.changeBackgroundColor()
    }

}
