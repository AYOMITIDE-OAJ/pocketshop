package com.oajstudios.pocketshop.activity

import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.Category
import com.oajstudios.pocketshop.models.RequestModel
import com.oajstudios.pocketshop.models.StoreProductModel
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.TotalItem.TOTAL_ITEM_PER_PAGE
import com.oajstudios.pocketshop.utils.Constants.TotalItem.TOTAL_SUB_CATEGORY_PER_PAGE
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.item_subcategory.view.*
import kotlinx.android.synthetic.main.item_viewproductgrid.view.*
import kotlinx.android.synthetic.main.toolbar.*

class SubCategoryActivity : AppBaseActivity() {
    var image: String = ""
    private var mCategoryId: Int = 0
    private var showPagination: Boolean? = true
    private var mIsLoading = false
    private var countLoadMore = 1
    private val data: MutableMap<String, Int> =
        HashMap()

    private val subCategoryData: MutableMap<String, Int> =
        HashMap()
    private var isLastPage: Boolean? = false

    private val mSubCategoryAdapter =
        BaseAdapter<Category>(R.layout.item_subcategory, onBind = { view, model, _ ->
            view.tvSubCategory.text = model.name.getHtmlString()
            if (model.image != null) {
                if (model.image.src.isNotEmpty()) {
                    view.ivProducts.loadImageFromUrl(model.image.src)
                }
            }
            view.tvSubCategory.changeTextSecondaryColor()
            view.llSubCategory.changeTint(getAccentColor())
            view.onClick {
                launchActivity<SubCategoryActivity> {
                    putExtra(Constants.KeyIntent.TITLE, model.name)
                    putExtra(Constants.KeyIntent.VIEWALLID, Constants.viewAllCode.CATEGORY)
                    putExtra(Constants.KeyIntent.KEYID, model.id)
                }
            }
        })

    private val mProductAdapter =
        BaseAdapter<StoreProductModel>(R.layout.item_viewproductgrid, onBind = { view, model, _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.ivProduct.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline!!.setRoundRect(0, 0, view!!.width, (view.height + 20F).toInt(), 20F)
                    }
                }
                view.ivProduct.clipToOutline = true
            }
            if (model.images!!.isNotEmpty()) {
                view.ivProduct.loadImageFromUrl(model.images!![0].src!!)
                image = model.images!![0].src!!
            }

            view.tvProductName.text = model.name?.getHtmlString()
            view.tvProductName.changeTextPrimaryColor()
            if (model.onSale) {
                view.tvSaleLabel.show()
            } else {
                view.tvSaleLabel.hide()
            }
            if (model.type!!.contains("grouped")) {
                view.tvDiscountPrice.hide()
                view.tvOriginalPrice.hide()
                view.tvAdd.hide()
            } else {
                if (!model.onSale) {
                    view.tvDiscountPrice.text = model.regularPrice!!.currencyFormat()
                    view.tvOriginalPrice.show()
                    if (model.regularPrice!!.isEmpty()) {
                        view.tvOriginalPrice.text = ""
                        view.tvDiscountPrice.text = model.price!!.currencyFormat()
                    } else {
                        view.tvOriginalPrice.text = ""
                        view.tvDiscountPrice.text = model.regularPrice!!.currencyFormat()
                    }
                } else {
                    if (model.salePrice!!.isNotEmpty()) {
                        view.tvDiscountPrice.text = model.salePrice!!.currencyFormat()
                    } else {
                        view.tvOriginalPrice.show()
                        view.tvDiscountPrice.text = model.price!!.currencyFormat()
                    }
                    view.tvOriginalPrice.applyStrike()
                    view.tvOriginalPrice.text = model.regularPrice!!.currencyFormat()
                    view.tvOriginalPrice.show()
                }
            }
            view.tvOriginalPrice.changeTextSecondaryColor()
            view.tvDiscountPrice.changePrimaryColorDark()
            view.tvAdd.changeBackgroundTint(getAccentColor())
            if (model.attributes!!.isNotEmpty()) {
                if (model.attributes!![0].options!!.isNotEmpty()) {
                    view.tvProductWeight.text = model.attributes?.get(0)?.options!![0]
                    view.tvProductWeight.changeAccentColor()
                }
            } else {
                view.tvProductWeight.text = ""
            }

            if (model.purchasable) {
                if (model.stockStatus == "instock") {
                    view.tvAdd.show()
                } else {
                    view.tvAdd.hide()
                }
            } else {
                view.tvAdd.hide()
            }

            view.tvAdd.onClick {
                addCart(model)
            }
            view.onClick {
                if (getProductDetailConstant() == 0) {
                    launchActivity<ProductDetailActivity1> {
                        putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)

                    }
                } else {
                    launchActivity<ProductDetailActivity2> {
                        putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)

                    }
                }
            }
        })

    private fun addCart(model: StoreProductModel) {
        if (isLoggedIn()) {
            val requestModel = RequestModel()
            if (model.type == "variable") {
                requestModel.pro_id = model.variations!![0]
            } else {
                requestModel.pro_id = model.id
            }
            requestModel.quantity = 1
            addItemToCart(requestModel, onApiSuccess = {
                fetchAndStoreCartData()
            })
        } else launchActivity<SignInUpActivity> { }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        setToolbar(toolbar)
        mCategoryId = intent.getIntExtra(Constants.KeyIntent.KEYID, -1)
        title = intent.getStringExtra(Constants.KeyIntent.TITLE)
        mAppBarColor()
        llMain.changeBackgroundColor()
        rvCategory.apply {
            setHorizontalLayout(false)
            setHasFixedSize(true)
            rvCategory.adapter = mSubCategoryAdapter
            rvCategory.rvItemAnimation()
        }
        data["page"] = countLoadMore
        data["per_page"] = TOTAL_ITEM_PER_PAGE
        data["category"] = mCategoryId
        loadCategory(data)

        subCategoryData["per_page"] = TOTAL_SUB_CATEGORY_PER_PAGE
        subCategoryData["parent"] = mCategoryId
        loadSubCategory(subCategoryData)

        rvNewestProduct.apply {
            layoutManager = GridLayoutManager(this@SubCategoryActivity, 2)
            setHasFixedSize(true)
            rvNewestProduct.adapter = mProductAdapter
            rvNewestProduct.rvItemAnimation()
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
                                data["per_page"] = TOTAL_ITEM_PER_PAGE
                                data["category"] = mCategoryId
                                loadCategory(data)
                            }
                        }
                    }
                })
            }
        }

    }

    private fun loadCategory(data: MutableMap<String, Int>) {
        if (isNetworkAvailable()) {
            showProgress(true)
            getRestApiImpl().listAllCategoryProduct(data, onApiSuccess = {
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

        } else {
            showProgress(false)
            noInternetSnackBar()
        }
    }

    private fun loadSubCategory(data: MutableMap<String, Int>) {
        if (isNetworkAvailable()) {
            showProgress(true)
            getRestApiImpl().listAllCategory(data, onApiSuccess = {
                showProgress(false)
                if (countLoadMore == 1) {
                    mSubCategoryAdapter.clearItems()
                }
                mIsLoading = false
                mSubCategoryAdapter.addMoreItems(it)
                if (mSubCategoryAdapter.itemCount == 0) {
                    rvCategory.hide()
                } else {
                    rvCategory.show()
                }
            }, onApiError = {
                showProgress(false)
                snackBar(it)
            })

        }
    }
}
