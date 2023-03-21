package com.oajstudios.pocketshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.activity.*
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.adapter.HomeSliderAdapter
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.SharedPref.CONTACT
import com.oajstudios.pocketshop.utils.Constants.SharedPref.COPYRIGHT_TEXT
import com.oajstudios.pocketshop.utils.Constants.SharedPref.DEFAULT_CURRENCY
import com.oajstudios.pocketshop.utils.Constants.SharedPref.DEFAULT_CURRENCY_FORMATE
import com.oajstudios.pocketshop.utils.Constants.SharedPref.ENABLE_COUPONS
import com.oajstudios.pocketshop.utils.Constants.SharedPref.FACEBOOK
import com.oajstudios.pocketshop.utils.Constants.SharedPref.INSTAGRAM
import com.oajstudios.pocketshop.utils.Constants.SharedPref.LANGUAGE
import com.oajstudios.pocketshop.utils.Constants.SharedPref.PAYMENT_METHOD
import com.oajstudios.pocketshop.utils.Constants.SharedPref.PRIVACY_POLICY
import com.oajstudios.pocketshop.utils.Constants.SharedPref.TERM_CONDITION
import com.oajstudios.pocketshop.utils.Constants.SharedPref.TWITTER
import com.oajstudios.pocketshop.utils.Constants.SharedPref.WHATSAPP
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_BANNER
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_BEST_SELLING
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_DEAL_OF_THE_DAY
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_FEATURED
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_NEWEST
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_OFFER
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_SALE
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_SUGGESTED_FOR_YOU
import com.oajstudios.pocketshop.utils.Constants.viewName.VIEW_YOU_MAY_LIKE
import com.oajstudios.pocketshop.utils.dotsindicator.DotsIndicator
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.dashboard_dealofferview.view.*
import kotlinx.android.synthetic.main.dashboard_item_category.view.*
import kotlinx.android.synthetic.main.dashboard_search.view.*
import kotlinx.android.synthetic.main.fragment_home2.*
import kotlinx.android.synthetic.main.item_home_dashboard2.view.tvSaleLabel
import kotlinx.android.synthetic.main.item_home_product_list.view.ivProduct
import kotlinx.android.synthetic.main.item_home_product_list.view.tvAdd
import kotlinx.android.synthetic.main.item_home_product_list.view.tvDiscountPrice
import kotlinx.android.synthetic.main.item_home_product_list.view.tvOriginalPrice
import kotlinx.android.synthetic.main.item_home_product_list.view.tvProductName
import kotlinx.android.synthetic.main.item_home_product_list.view.tvProductWeight
import kotlinx.android.synthetic.main.menu_cart.view.*
import java.util.*
import kotlin.collections.HashMap

class HomeFragment2 : BaseFragment() {
    private var mMenuCart: View? = null
    private var image: String = ""
    private var isAddedToCart: Boolean = false
    private lateinit var lan: String
    private var mViewNewest: View? = null
    private var mViewFeatured: View? = null
    private var mViewDealOfTheDay: View? = null
    private var mViewOffer: View? = null
    private var mViewBestSelling: View? = null
    private var mViewSale: View? = null
    private var mViewYouMayLike: View? = null
    private var mViewSuggested: View? = null
    private var mViewCategory: View? = null
    private var mViewSearch: View? = null
    private var mSliderView: View? = null
    private var mLLDynamic: LinearLayout? = null
    private lateinit var mDashboardJson: BuilderDashboard
    private val data: MutableMap<String, Int> =
            HashMap()

    private fun setProductItem(
            view: View,
            model: StoreProductModel,
            params: Boolean = false
    ) {

        if (model.images!![0].src!!.isNotEmpty()) {
            view.ivProduct.loadImageFromUrl(model.images!![0].src!!)
            image = model.images!![0].src!!
        }
        if (!params) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.ivProduct.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline!!.setRoundRect(0, 0, view!!.width, (view.height + 20F).toInt(), 20F)
                    }
                }
                view.ivProduct.clipToOutline = true
            }
        } else {
            view.ivProduct.layoutParams = activity?.productLayoutParamsForDealOffer()
        }
        val mName = model.name!!.split(",")
        view.tvProductName.text = mName[0]
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
            view.tvOriginalPrice.changeTextSecondaryColor()
            view.tvDiscountPrice.changeTextPrimaryColor()

            view.tvAdd.background.setTint(Color.parseColor(getAccentColor()))
            if (model.attributes!!.isNotEmpty()) {
                if (model.attributes!![0].options!!.isNotEmpty()) {
                    view.tvProductWeight.text = model.attributes!![0].options!![0]
                    view.tvProductWeight.changeTextSecondaryColor()
                }
            }
            if (model.in_stock) {
                view.tvAdd.show()
            } else {
                view.tvAdd.hide()
            }
            if (!model.purchasable) {
                view.tvAdd.hide()
            } else {
                view.tvAdd.show()
            }
        }

        view.tvOriginalPrice.changeTextSecondaryColor()
        view.tvDiscountPrice.changeTextPrimaryColor()
        view.tvAdd.background.setTint(Color.parseColor(getAccentColor()))
        if (model.attributes!!.isNotEmpty()) {
            view.tvProductWeight.text = model.attributes!![0].options!![0]
            view.tvProductWeight.changeAccentColor()
        }
        if (model.in_stock) {
            view.tvAdd.show()
        } else {
            view.tvAdd.hide()
        }
        if (!model.purchasable) {
            view.tvAdd.hide()
        } else {
            view.tvAdd.show()
        }
        view.onClick {
            if (getProductDetailConstant() == 0) {
                activity?.launchActivity<ProductDetailActivity1> {
                    putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)
                    putExtra(Constants.KeyIntent.DATA, model)
                }
            } else {
                activity?.launchActivity<ProductDetailActivity2> {
                    putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)
                    putExtra(Constants.KeyIntent.DATA, model)
                }
            }
        }
        view.tvAdd.onClick {
            mAddCart(model)
        }
    }

    private val mProductAdapter =
            BaseAdapter<Category>(R.layout.dashboard_item_category, onBind = { view, model, _ ->

                val androidColors = resources.getIntArray(R.array.categoryColor)
                val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]

                view.ivBackground.loadImageFromUrl("https://bestwomenapparels.com/wp-content/uploads/2019/05/bestwomenapparels-fashion.jpg")
                view.mViewBackground.background.colorFilter =
                        PorterDuffColorFilter(randomAndroidColor, PorterDuff.Mode.SRC_IN)

                view.tvCategories.text = model.name.getHtmlString()
                view.onClick {
                    launchActivity<ViewAllProductActivity> {
                        putExtra(Constants.KeyIntent.TITLE, model.name)
                        putExtra(Constants.KeyIntent.VIEWALLID, Constants.viewAllCode.CATEGORY)
                        putExtra(Constants.KeyIntent.KEYID, model.id)
                    }
                }
            })

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home2, container, false)


    private fun mAddCart(model: StoreProductModel) {
        if (isLoggedIn()) {
            val requestModel = RequestModel()
            if (model.type == "variable") {
                requestModel.pro_id = model.variations!![0]
            } else {
                requestModel.pro_id = model.id
            }
            requestModel.quantity = 1
            (activity as AppBaseActivity).addItemToCart(requestModel, onApiSuccess = {
                isAddedToCart = true
                requireActivity().fetchAndStoreCartData()
            })
        } else (activity as AppBaseActivity).launchActivity<SignInUpActivity> { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if (isLoggedIn()) {
            loadApis()
        }
        listAllProducts()
        refreshLayout.setOnRefreshListener {
            dashboardMainView!!.removeAllViews()
            listAllProducts()
            refreshLayout.isRefreshing = false
        }
        refreshLayout.viewTreeObserver.addOnScrollChangedListener {
        }
        mLLDynamic = view.findViewById(R.id.dashboardMainView)
        scrollView.changeBackgroundColor()

        mDashboardJson = getBuilderDashboard()
        mSliderUI()
        mProductUI()

    }


    @SuppressLint("InflateParams")
    private fun mProductUI() {
        val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mViewNewest = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewFeatured = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewDealOfTheDay = inflater.inflate(R.layout.dashboard_dealofferview, null)
        mViewOffer = inflater.inflate(R.layout.dashboard_dealofferview, null)
        mViewBestSelling = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewSale = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewYouMayLike = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewSuggested = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewCategory = inflater.inflate(R.layout.dashboard_category, null)
        mViewSearch = inflater.inflate(R.layout.dashboard_search, null)
    }

    @SuppressLint("InflateParams")
    private fun mSliderUI() {
        val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mSliderView = inflater.inflate(R.layout.dashboard_sliderview, null)
    }

    private fun onAddCategory(mView: View?) {
        val recyclerView = mView!!.findViewById(R.id.rvCategory) as RecyclerView
        recyclerView.apply {
            setHorizontalLayout()
            setHasFixedSize(true)
            adapter = mProductAdapter
            rvItemAnimation()
        }
    }

    private fun onAddSearch(mView: View?) {
        val mSearch = mView!!.findViewById(R.id.edtSearch) as EditText
        mView.rlMain.changeTint(getPrimaryColor())
        mSearch.onClick {
            launchActivity<SearchActivity> { }
        }
    }

    private fun onAddView(
            mView: View?,
            isGridView: Boolean = false,
            title: String,
            mViewAll: String,
            code: Int,
            specialKey: String = "",
            productList: List<StoreProductModel>,
            modelSize: Int = 5
    ) {
        val recyclerView = mView!!.findViewById(R.id.rvNewProduct) as RecyclerView
        val viewAllProduct = mView.findViewById(R.id.viewAllItem) as TextView
        val titleProduct = mView.findViewById(R.id.tvTitleBar) as TextView

        if (mView == mViewDealOfTheDay || mView == mViewOffer) {
            mView.llDeal.changeTint(getPrimaryColor())
            mView.viewAllItem.changeAccentColor()
            titleProduct.changeTitleColor()
        } else {
            mView.viewAllItem.changeTextSecondaryColor()
            titleProduct.changeAccentColor()
        }
        mView.viewAllItem.text = mViewAll
        titleProduct.text = title
        titleProduct.textSize = 20F

        if (isGridView) {
            val productAdapter = BaseAdapter<StoreProductModel>(
                    R.layout.item_home_product_list,
                    onBind = { view, model, _ ->
                        setProductItem(view, model, true)
                    })
            productAdapter.addItems(productList)
            productAdapter.setModelSize(modelSize)
            productAdapter.setModelSize(4)

            recyclerView.adapter = productAdapter
            recyclerView.setVerticalLayout()

        } else {
            val productAdapter = BaseAdapter<StoreProductModel>(
                    R.layout.item_home_dashboard2,
                    onBind = { view, model, _ ->
                        setProductItem(view, model, false)
                    })
            productAdapter.addItems(productList)
            productAdapter.setModelSize(modelSize)

            recyclerView.setHorizontalLayout()
            recyclerView.adapter = productAdapter
        }
        viewAllProduct.onClick {
            activity?.launchActivity<ViewAllProductActivity> {
                putExtra(Constants.KeyIntent.TITLE, title)
                putExtra(Constants.KeyIntent.VIEWALLID, code)
                putExtra(Constants.KeyIntent.SPECIAL_PRODUCT_KEY, specialKey)
            }
        }
        if (mView.parent != null) {
            (mView.parent as ViewGroup).removeView(mView) // <- fix
        }
    }

    private fun addSlider(
            productList: List<DashboardBanner>
    ) {
        val slideViewPager = mSliderView!!.findViewById(R.id.slideViewPager) as ViewPager
        val dots = mSliderView!!.findViewById(R.id.dots) as DotsIndicator

        val adapter1 = HomeSliderAdapter(productList)
        slideViewPager.adapter = adapter1
        adapter1.setListener(object : HomeSliderAdapter.OnClickListener {
            override fun onClick(position: Int, mImg: List<DashboardBanner>) {
                launchActivity<WebViewExternalProductActivity> {
                    putExtra(Constants.KeyIntent.EXTERNAL_URL, mImg[position].url)
                    putExtra(Constants.KeyIntent.IS_BANNER, true)
                }
            }
        })
        dots.attachViewPager(slideViewPager)
        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)

        mLLDynamic!!.addView(mSliderView!!)
    }


    private fun showLoader() {
        (activity as AppBaseActivity).showProgress(true)
    }

    private fun loadApis() {
        if (isNetworkAvailable()) {
            requireActivity().fetchAndStoreCartData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_dashboard, menu)
        val menuCartItem: MenuItem = menu.findItem(R.id.action_cart)
        val menuSearchItem: MenuItem = menu.findItem(R.id.action_search)
        menuCartItem.isVisible = true
        menuSearchItem.isVisible = false
        mMenuCart = menuCartItem.actionView
        mMenuCart?.onClick {
            if (isLoggedIn()) {
                launchActivity<MyCartActivity>()
            } else {
                launchActivity<SignInUpActivity>()
            }
        }
        setCartCount()
    }

    fun setCartCount() {
        val count = getCartCount()
        mMenuCart?.ivCart?.changeBackgroundImageTint(getTextTitleColor())
        mMenuCart?.tvNotificationCount?.changeTint(getTextTitleColor())
        mMenuCart?.tvNotificationCount?.text = count
        mMenuCart?.tvNotificationCount?.changeAccentColor()
        if (count.checkIsEmpty() || count == "0") {
            mMenuCart?.tvNotificationCount?.hide()
        } else {
            mMenuCart?.tvNotificationCount?.show()
        }
    }

    private fun listAllProducts() {
        if (isNetworkAvailable()) {
            showLoader()
            getRestApiImpl().getDashboardData(onApiSuccess = {
                if (activity == null) return@getDashboardData
                (activity as AppBaseActivity).showProgress(false)
                getSharedPrefInstance().apply {
                    removeKey(WHATSAPP)
                    removeKey(FACEBOOK)
                    removeKey(TWITTER)
                    removeKey(INSTAGRAM)
                    removeKey(CONTACT)
                    removeKey(PRIVACY_POLICY)
                    removeKey(TERM_CONDITION)
                    removeKey(COPYRIGHT_TEXT)
                    removeKey(LANGUAGE)
                    //setValue(LANGUAGE, it.app_lang)
                    setValue(DEFAULT_CURRENCY, it.currency_symbol.currency_symbol)
                    setValue(DEFAULT_CURRENCY_FORMATE, it.currency_symbol.currency)
                    setValue(WHATSAPP, it.social_link.whatsapp)
                    setValue(FACEBOOK, it.social_link.facebook)
                    setValue(TWITTER, it.social_link.twitter)
                    setValue(INSTAGRAM, it.social_link.instagram)
                    setValue(CONTACT, it.social_link.contact)
                    setValue(PRIVACY_POLICY, it.social_link.privacy_policy)
                    setValue(TERM_CONDITION, it.social_link.term_condition)
                    setValue(COPYRIGHT_TEXT, it.social_link.copyright_text)
                    setValue(ENABLE_COUPONS, it.enable_coupons)
                    setValue(PAYMENT_METHOD, it.payment_method)
                }
                //setNewLocale(it.app_lang)


                onAddCategory(mView = mViewCategory)
                onAddSearch(mView = mViewSearch)
                mLLDynamic!!.addView(mViewSearch!!)
                mLLDynamic!!.addView(mViewCategory!!)

                for (view in mDashboardJson.sorting!!) {
                    when (view) {
                        VIEW_BANNER -> {
                            if (it.banner.isNotEmpty()) {
                                if (mDashboardJson.sliderView!!.enable == true) {
                                    addSlider(
                                            productList = it.banner
                                    )
                                }
                            }
                        }
                        VIEW_NEWEST -> {
                            if (it.newest.isNotEmpty()) {
                                if (mDashboardJson.newProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewNewest,
                                            title = mDashboardJson.newProduct!!.title!!,
                                            mViewAll = mDashboardJson.newProduct!!.viewAll!!,
                                            code = Constants.viewAllCode.NEWEST,
                                            productList = it.newest,
                                            modelSize = 5
                                    )
                                    mLLDynamic!!.addView(mViewNewest!!)
                                }
                            }
                        }
                        VIEW_FEATURED -> {
                            if (it.featured.isNotEmpty()) {
                                if (mDashboardJson.feature!!.enable == true) {
                                    onAddView(
                                            mView = mViewFeatured,
                                            title = mDashboardJson.feature!!.title!!,
                                            mViewAll = mDashboardJson.feature!!.viewAll!!,
                                            code = Constants.viewAllCode.FEATURED,
                                            productList = it.featured,
                                            modelSize = 5
                                    )
                                    mLLDynamic!!.addView(mViewFeatured!!)
                                }
                            }
                        }
                        VIEW_DEAL_OF_THE_DAY -> {
                            if (it.deal_of_the_day.isNotEmpty()) {
                                if (mDashboardJson.dealOfTheDay!!.enable == true) {
                                    onAddView(
                                            mView = mViewDealOfTheDay,
                                            title = mDashboardJson.dealOfTheDay!!.title!!,
                                            mViewAll = mDashboardJson.dealOfTheDay!!.viewAll!!,
                                            code = Constants.viewAllCode.SPECIAL_PRODUCT,
                                            productList = it.deal_of_the_day,
                                            isGridView = true,
                                            modelSize = 2,
                                            specialKey = "deal_of_the_day"
                                    )
                                    mLLDynamic!!.addView(mViewDealOfTheDay!!)
                                }
                            }
                        }
                        VIEW_BEST_SELLING -> {
                            if (it.best_selling_product.isNotEmpty()) {
                                if (mDashboardJson.bestSaleProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewBestSelling,
                                            title = mDashboardJson.bestSaleProduct!!.title!!,
                                            mViewAll = mDashboardJson.bestSaleProduct!!.viewAll!!,
                                            code = Constants.viewAllCode.BESTSELLING,
                                            productList = it.best_selling_product,
                                            modelSize = 5
                                    )
                                    mLLDynamic!!.addView(mViewBestSelling!!)
                                }
                            }
                        }
                        VIEW_SALE -> {
                            if (it.sale_product.isNotEmpty()) {
                                if (mDashboardJson.saleProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewSale,
                                            title = mDashboardJson.saleProduct!!.title!!,
                                            mViewAll = mDashboardJson.saleProduct!!.viewAll!!,
                                            code = Constants.viewAllCode.SALE,
                                            productList = it.sale_product,
                                            modelSize = 5
                                    )
                                    mLLDynamic!!.addView(mViewSale!!)
                                }
                            }
                        }
                        VIEW_OFFER -> {
                            if (it.offer.isNotEmpty()) {
                                if (mDashboardJson.offerProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewOffer,
                                            title = mDashboardJson.offerProduct!!.title!!,
                                            mViewAll = mDashboardJson.offerProduct!!.viewAll!!,
                                            isGridView = true,
                                            code = Constants.viewAllCode.SPECIAL_PRODUCT,
                                            productList = it.offer,
                                            modelSize = 2,
                                            specialKey = "offer"
                                    )
                                    mLLDynamic!!.addView(mViewOffer!!)
                                }
                            }
                        }
                        VIEW_SUGGESTED_FOR_YOU -> {
                            if (it.suggested_for_you.isNotEmpty()) {
                                if (mDashboardJson.suggestionProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewSuggested,
                                            title = mDashboardJson.suggestionProduct!!.title!!,
                                            mViewAll = mDashboardJson.suggestionProduct!!.viewAll!!,
                                            code = Constants.viewAllCode.SPECIAL_PRODUCT,
                                            productList = it.suggested_for_you,
                                            modelSize = 5,
                                            specialKey = "suggested_for_you"
                                    )
                                    mLLDynamic!!.addView(mViewSuggested!!)
                                }
                            }
                        }
                        VIEW_YOU_MAY_LIKE -> {
                            if (it.you_may_like.isNotEmpty()) {
                                if (mDashboardJson.youMayLikeProduct!!.enable == true) {
                                    onAddView(
                                            mView = mViewYouMayLike,
                                            title = mDashboardJson.youMayLikeProduct!!.title!!,
                                            mViewAll = mDashboardJson.youMayLikeProduct!!.viewAll!!,
                                            code = Constants.viewAllCode.SPECIAL_PRODUCT,
                                            productList = it.you_may_like,
                                            modelSize = 5,
                                            specialKey = "you_may_like"
                                    )
                                    mLLDynamic!!.addView(mViewYouMayLike!!)
                                }

                            }
                        }
                    }
                }

            }, onApiError = {
                (activity as AppBaseActivity).showProgress(false)
                snackBar(it)
            })
            data["per_page"] = Constants.TotalItem.TOTAL_CATEGORY_PER_PAGE
            getRestApiImpl().listAllCategory(data, onApiSuccess = {
                mProductAdapter.addMoreItems(it)
            }, onApiError = {
                (activity as AppBaseActivity).showProgress(false)
                snackBar(it)
            })
        }
    }

   /* private fun setNewLocale(language: String) {
       // WooBoxApp.changeLanguage(language)
        lan = WooBoxApp.language
        if (lan != language) {
            (activity as AppBaseActivity).recreate()
        }
    }*/


}