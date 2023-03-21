package com.oajstudios.pocketshop.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.*
import android.widget.Toast
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.activity.*
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.EXTRA_ADD_AMOUNT
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.DISCOUNT
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.DISCOUNT_MRP
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.PRICE
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.PRODUCTDATA
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.SHIPPING
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.SHIPPINGDATA
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.SUBTOTAL
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.TOTAL
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_shipping_method.view.*

class MyCartFragment : BaseFragment() {

    private var mCoupons: Coupons? = null
    private var mTotalCount = 0.0
    private var mTotalMRPCount = 0.0
    private var mSubTotal = 0.0
    private var mTotalDiscount = "0"
    private var mTotalDiscountMRP = 0.0
    private var mShippingCost = "0"
    private var cartItemId = ""
    private var isRemoveCoupons = true
    private var mOrderItems: ArrayList<Line_items>? = ArrayList()
    private var shippingMethods = ArrayList<Method>()
    private var shippingMethodsAvailable = ArrayList<Method>()
    private var shipping: Shipping? = null
    private var selectedMethod = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    private val mCartAdapter =
        BaseAdapter<CartResponse>(R.layout.item_cart, onBind = { view, model, position ->
            view.tvProductName.text = model.name

            view.ivProduct.loadImageFromUrl(model.full)

            if (model.sale_price.isNotEmpty() && model.on_sale) {
                view.tvOriginalPrice.text =
                    (model.regular_price.toFloat().toInt() * model.quantity.toInt()).toString()
                        .currencyFormat()
                view.tvOriginalPrice.applyStrike()
                view.tvPrice.text = (model.sale_price.toFloat().toInt() * model.quantity.toInt()).toString()
                    .currencyFormat()
            } else {
                view.tvPrice.text =
                    (model.regular_price.toFloat().toInt() * model.quantity.toInt()).toString()
                        .currencyFormat()

            }

            view.qty_spinner.text = model.quantity

            view.delete_layout.onClick { view.swipeLayout.close(true); removeCartItem(model) }

            view.ivMinus.onClick { minusClick(model, position) }

            view.ivAdd.onClick { addClick(model, position) }

            view.front_layout.onClick {
                if (getProductDetailConstant() == 0) {
                    activity?.launchActivity<ProductDetailActivity1> {
                        putExtra(Constants.KeyIntent.PRODUCT_ID, model.pro_id)
                    }
                } else {
                    activity?.launchActivity<ProductDetailActivity2> {
                        putExtra(Constants.KeyIntent.PRODUCT_ID, model.pro_id)
                    }
                }
            }

            view.tvProductName.changeTextPrimaryColor()
            view.qty_spinner.changeTextPrimaryColor()
            view.ivMinus.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getPrimaryColorDark()))
            view.ivAdd.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getPrimaryColorDark()))
            view.tvPrice.changeTextPrimaryColor()
            view.tvOriginalPrice.changeTextSecondaryColor()
            view.delete_layout.setCardBackgroundColor(Color.parseColor(getButtonColor()))
        })

    @SuppressLint("SetTextI18n")
    private val mShippingMethodAdapter =
        BaseAdapter<Method>(R.layout.item_shipping_method, onBind = { view, model, position ->
            if (model.id == "free_shipping" || model.cost == "0" || model.cost.isEmpty()) {
                view.shippingMethod.text = model.methodTitle
                view.shippingCost.hide()
            } else {
                view.shippingMethod.text = model.methodTitle + ": "
                view.shippingCost.text = model.cost.currencyFormat()
            }
            if (selectedMethod == position) {
                view.imgDone.setImageResource(R.drawable.ic_baseline_done_24)
            } else {
                view.imgDone.setImageResource(0)
            }
            view.shippingMethod.changeTextSecondaryColor()
            view.shippingCost.changeTextPrimaryColor()
        })

    private fun addClick(model: CartResponse, position: Int) {
        val qty = model.quantity.toInt()
        mCartAdapter.items[position].quantity = qty.plus(1).toString()
        mCartAdapter.notifyItemChanged(position)
        removeCoupon()
        updateCartItem(mCartAdapter.items[position])
    }

    private fun minusClick(model: CartResponse, position: Int) {
        val qty = model.quantity.toInt()
        if (qty > 1) {
            mCartAdapter.items[position].quantity = qty.minus(1).toString()
            mCartAdapter.notifyItemChanged(position)
            removeCoupon()
            updateCartItem(mCartAdapter.items[position])
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        changeColor()
        tvContinue.onClick {
            if (shipping?.country!!.isNotEmpty()) {
                var selectedShippingMethod: Method? = null
                val mAmount = mTotalCount + mShippingCost.toDouble()
                if (shippingMethodsAvailable.isNotEmpty()) {
                    if (selectedMethod == -1) {
                        activity?.toast("Select Shipping Method")
                        return@onClick
                    }
                    selectedShippingMethod = shippingMethodsAvailable[selectedMethod]
                }
                Log.d("Total Amount", mAmount.toString())
                launchActivity<OrderSummaryActivity>(Constants.RequestCode.ORDER_SUMMARY) {
                    if (mCoupons != null) {
                        putExtra(Constants.KeyIntent.COUPON_CODE, mCoupons)
                    }
                    putExtra(PRICE, mAmount.toString())
                    putExtra(PRODUCTDATA, mOrderItems)
                    putExtra(SHIPPINGDATA, selectedShippingMethod)
                    putExtra(SUBTOTAL, mSubTotal.toString())
                    putExtra(TOTAL, mTotalMRPCount.toString())
                    putExtra(DISCOUNT, mTotalDiscount)
                    putExtra(SHIPPING, mShippingCost)
                    putExtra(DISCOUNT_MRP, mTotalDiscountMRP)
                }
            } else {
                Toast.makeText(context, "You do not provided shipping address.", Toast.LENGTH_SHORT).show()
            }
        }
        tvChange.onClick {
            launchActivity<EditProfileActivity>(requestCode = Constants.RequestCode.EDIT_PROFILE)
        }
        rvCart.setVerticalLayout()
        rvCart.adapter = mCartAdapter
        rvShippingMethod.setVerticalLayout()
        rvShippingMethod.adapter = mShippingMethodAdapter
        mShippingMethodAdapter.onItemClick = { pos, _, model ->
            onShippingMethodChanged(pos, model)
        }
        btnShopNow.onClick { launchActivity<DashBoardActivity> { } }

        if (getSharedPrefInstance().getBooleanValue(Constants.SharedPref.ENABLE_COUPONS, false)) {
            rlCoupon.show()
            llCoupon.show()
        } else {
            rlCoupon.hide()
            llCoupon.hide()
        }
        invalidateCartLayout()
    }

    private fun onShippingMethodChanged(pos: Int, model: Method) {

        activity?.runOnUiThread {
            selectedMethod = pos
            mShippingMethodAdapter.notifyDataSetChanged()
            if (model.id == "free_shipping") {
                tvShipping.text = "Free"
                mShippingCost = "0"
                tvShipping.setTextColor(requireActivity().color(R.color.green))
                llShippingAmount.show()
                Log.d("mTotalCount", mTotalCount.toString())
            } else {
                if (model.cost.isEmpty()) {
                    model.cost = "0"
                }
                mShippingCost = model.cost
                llShippingAmount.show()
                tvShipping.changeTextSecondaryColor()
                tvShipping.text = mShippingCost.currencyFormat()
            }
            tvTotalCartAmount.text =
                (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_cart, menu)
        val positionOfMenuItem = 0
        val item = menu.getItem(positionOfMenuItem)
        val s = SpannableString(getString(R.string.lbl_wish_list))
        item.title = s
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_wishlist -> {
                if (activity is DashBoardActivity) {
                    (activity as DashBoardActivity).loadWishlistFragment()
                } else {
                    activity?.launchActivity<WishListActivity>(requestCode = Constants.RequestCode.WISHLIST)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun invalidatePaymentLayout(b: Boolean) {
        if (activity != null) {
            if (!b) {
                lay_button.hide()
                rvCart.hide()
                llShipping.hide()
            } else {
                lay_button.show()
                llShipping.show()
                rvCart.show()
            }
        }
    }

    private fun removeMultipleCartItem() {
        (activity as AppBaseActivity).showProgress(true)
        val requestModel = CartRequestModel()
        requestModel.multpleId = cartItemId
        (activity as AppBaseActivity).removeMultipleCartItem(requestModel, onApiSuccess = {
            (activity as AppBaseActivity).showProgress(false)
            requireActivity().finish()
        })
    }

    private fun removeCartItem(model: CartResponse) {
        val requestModel = RequestModel()
        requestModel.pro_id = model.pro_id
        (activity as AppBaseActivity).removeCartItem(requestModel, onApiSuccess = {
            invalidateCartLayout()
        })
    }

    @SuppressLint("SetTextI18n")
    fun invalidateCartLayout() {
        if (isNetworkAvailable()) {
            (activity as AppBaseActivity).showProgress(true)
            getRestApiImpl().getCart(onApiSuccess = {
                if (activity == null) return@getCart
                getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, it.total_quantity)
                (activity as AppBaseActivity).sendCartCountChangeBroadcast()
                if (it.total_quantity == 0) {
                    invalidatePaymentLayout(false)
                    (activity as AppBaseActivity).showProgress(false)

                    llNoItems.show()
                    lay_button.hide()
                    llShipping.hide()
                    nsvCart.hide()
                } else {
                    llNoItems.hide()
                    lay_button.show()
                    llShipping.show()
                    nsvCart.show()
                    if (activity != null) {
                        llNoItems.hide()
                        cartItemId = ""
                        mTotalCount = 0.0
                        mTotalMRPCount = 0.0
                        mTotalDiscountMRP = 0.0
                        mOrderItems!!.clear()
                        for (i in 0 until it.data.size) {
                            val itemData = Line_items()
                            itemData.product_id = it.data[i].pro_id
                            itemData.quantity = it.data[i].quantity.toInt()
                            mOrderItems!!.add(itemData)
                            mTotalDiscountMRP +=
                                when {
                                    it.data[i].sale_price.isNotEmpty() && it.data[i].on_sale -> {
                                        (it.data[i].regular_price.toDouble() - it.data[i].sale_price.toDouble()) * it.data[i].quantity.toInt()
                                    }
                                    else -> 0.0
                                }

                            mTotalCount += try {
                                when {
                                    it.data[i].sale_price.isNotEmpty() -> {
                                        it.data[i].sale_price.toFloat().toInt() * it.data[i].quantity.toInt()
                                    }
                                    it.data[i].regular_price.isNotEmpty() -> {
                                        it.data[i].regular_price.toFloat().toInt() * it.data[i].quantity.toInt()
                                    }
                                    else -> {
                                        it.data[i].price.toFloat().toInt() * it.data[i].quantity.toInt()
                                    }
                                }
                            } catch (e: Exception) {
                                it.data[i].price.toFloat().toInt() * it.data[i].quantity.toInt()
                            }
                            mTotalMRPCount += try {
                                when {
                                    it.data[i].regular_price.isNotEmpty() -> {
                                        it.data[i].regular_price.toFloat().toInt() * it.data[i].quantity.toInt()
                                    }
                                    else -> {
                                        it.data[i].price.toFloat().toInt() * it.data[i].quantity.toInt()
                                    }
                                }
                            } catch (e: Exception) {
                                it.data[i].price.toFloat().toInt() * it.data[i].quantity.toInt()
                            }
                            cartItemId = if (cartItemId.isNotEmpty()) {
                                cartItemId + "," + itemData.product_id.toString()
                            } else {
                                itemData.product_id.toString()
                            }


                        }
                        mSubTotal = mTotalCount
                        tvTotal.text = mTotalMRPCount.toString().currencyFormat()
                        if (mTotalDiscountMRP <= 0.0) {
                            llMRPDiscount.hide()
                        } else {
                            tvDiscountMRPTotal.text = "- " + mTotalDiscountMRP.toString().currencyFormat()
                            llMRPDiscount.show()
                        }
                        if (isRemoveCoupons) {
                            tvDiscount.text = "0".currencyFormat()
                            tvTotalCartAmount.text =
                                (mTotalCount.toInt() + mShippingCost.toInt()).toString()
                                    .currencyFormat()
                            tvEditCoupon.text = getString(R.string.lbl_apply)
                            tvEditCoupon.onClick {
                                launchActivity<CouponActivity>(Constants.RequestCode.COUPON_CODE) { }
                            }
                        } else {
                            applyCouponCode()
                        }
                        invalidatePaymentLayout(true)
                        mCartAdapter.clearItems()
                        mCartAdapter.addItems(it.data)

                        fetchShippingMethods()

                    }
                }

            }, onApiError = {
                if (activity == null) return@getCart
                (activity as AppBaseActivity).showProgress(false)
                getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, 0)
                (activity as AppBaseActivity).sendCartCountChangeBroadcast()
                llNoItems.show()
                lay_button.hide()
                llShipping.hide()
                nsvCart.hide()
            })
        } else {
            if (activity == null) return
            (activity as AppBaseActivity).noInternetSnackBar()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchShippingMethods() {
        val requestModel = RequestModel()
        if (shipping == null) {
            shipping = getShippingList()
        }
        tvAddress.text = "(${shipping?.getFullAddress()})"
        if (shipping?.country!!.isNotEmpty()) {
            fetchCountry(onApiSuccess = {
                it.forEach { country ->
                    if (country.name == shipping?.country) {
                        requestModel.country_code = country.code
                        if (shipping?.state!!.isNotEmpty()) {
                            country.states.forEach { state ->
                                if (state.name == shipping?.state) {
                                    requestModel.state_code = state.code
                                }
                            }
                        }
                    }
                }
                requestModel.postcode = shipping?.postcode

                getRestApiImpl().getShippingMethod(requestModel, onApiSuccess = { shippingModel ->
                    hideProgress()
                    shippingMethods.clear()
                    if (shippingModel.methods != null) {
                        shippingMethods.addAll(shippingModel.methods!!)
                    }
                    invalidateShippingMethods()
                }, onApiError = {
                    (activity as AppBaseActivity).snackBar(it)
                    (activity as AppBaseActivity).showProgress(false)

                })
            }, onApiError = {
                (activity as AppBaseActivity).showProgress(false)

            })
        } else {
            hideProgress()
            tvAddress.text = "You do not provided shipping address."
            llShippingAmount.hide()
            // tvFreeShipping.show()
        }
    }

    private fun invalidateShippingMethods() {
        shippingMethodsAvailable.clear()
        mShippingMethodAdapter.clearItems()
        if (shippingMethods.isEmpty()) {
            llShippingAmount.hide()
            tvFreeShipping.show()
        } else {
            if (llShippingAmount != null) {
                llShippingAmount.hide()
                tvFreeShipping.hide()
            }
            shippingMethods.forEachIndexed { _, attr ->
                if (attr.enabled == "yes") {
                    if (attr.id == "free_shipping") {
                        if (isMethodAvailable(attr)) {
                            shippingMethodsAvailable.add(attr)
                        }
                    } else {
                        shippingMethodsAvailable.add(attr)
                    }
                }
            }
            mShippingMethodAdapter.addItems(shippingMethodsAvailable)
            if (shippingMethodsAvailable.isEmpty()) {
                tvFreeShipping.show()
            } else {
                onShippingMethodChanged(0, shippingMethodsAvailable[0])
            }
        }
    }


    private fun isMethodAvailable(method: Method): Boolean {
        return when (method.requires) {
            "either" -> minAmount(method) || coupan()
            "both" -> minAmount(method) && coupan()
            "min_amount" -> minAmount(method)
            "coupon" -> coupan()
            else -> true
        }

    }

    private fun minAmount(method: Method): Boolean {
        return if (method.ignoreDiscounts == "yes") {
            mSubTotal >= method.minAmount.toDouble()
        } else {
            mTotalCount >= method.minAmount.toDouble()
        }
    }

    private fun coupan(): Boolean {
        return !isRemoveCoupons && mCoupons != null && mCoupons?.free_shipping!!
    }

    private fun updateCartItem(model: CartResponse) {
        if (activity == null) return
        (activity as AppBaseActivity).showProgress(true)
        if (isNetworkAvailable()) {
            if (activity == null) return
            (activity as AppBaseActivity).showProgress(true)
            val requestModel = RequestModel()
            requestModel.pro_id = model.pro_id
            requestModel.cartid = model.cart_id.toInt()
            requestModel.quantity = model.quantity.toInt()

            getRestApiImpl().updateItemInCart(request = requestModel, onApiSuccess = {
                if (activity == null) return@updateItemInCart
                (activity as AppBaseActivity).showProgress(false)
                invalidateCartLayout()

            }, onApiError = {
                if (activity == null) return@updateItemInCart
                (activity as AppBaseActivity).showProgress(false)
                snackBar(it)
            })

        } else {
            if (activity == null) return
            (activity as AppBaseActivity).showProgress(false)
            (activity as AppBaseActivity).noInternetSnackBar()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.COUPON_CODE -> {
                    mCoupons = data!!.getSerializableExtra("couponData") as Coupons
                    isRemoveCoupons = false
                    applyCouponCode()
                }
                Constants.RequestCode.ORDER_SUMMARY -> {
                    removeMultipleCartItem()
                }
                Constants.RequestCode.EDIT_PROFILE -> {
                    shipping = null
                    fetchShippingMethods()
                }
                Constants.RequestCode.WISHLIST -> {
                    invalidateCartLayout()
                }
                else -> {
                    activity?.finish()
                }
            }
        }
    }

    /**
     * Apply coupon code
     *
     */
    @SuppressLint("SetTextI18n")
    private fun applyCouponCode() {
        if (mCoupons != null) {
            mTotalDiscount = "0"
            if (mCoupons!!.minimum_amount.toFloat() > 0.0) {
                if (mTotalCount < mCoupons!!.minimum_amount.toFloat()) {
                    txtApplyCouponCode.text =
                        getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.minimum_amount.currencyFormat() + getString(
                            R.string.lbl_coupon_is_valid_only_orders_of1
                        )
                    return
                } else if (mCoupons!!.maximum_amount.toFloat() > 0.0) {
                    if (mTotalCount > mCoupons!!.maximum_amount.toFloat()) {
                        txtApplyCouponCode.text =
                            getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.maximum_amount.currencyFormat() + " and below."
                        return
                    }
                }
            } else if (mCoupons!!.maximum_amount.toFloat() > 0.0) {
                if (mTotalCount > mCoupons!!.maximum_amount.toFloat()) {
                    txtApplyCouponCode.text =
                        getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.maximum_amount.currencyFormat() + " and below."
                    return
                }
            } else if (mCoupons?.discount_type == "fixed_cart") {
                if (mTotalCount < mCoupons!!.amount.toFloat()) {
                    txtApplyCouponCode.text =
                        getString(R.string.lbl_coupon_is_valid_only_orders_of) + (mCoupons!!.amount.toFloat() + EXTRA_ADD_AMOUNT) + " and above. Try other coupon."
                    return
                }
            } else if (mCoupons?.discount_type == "fixed_product") {
                var isValidCoupon = true
                for (i in 0 until mCartAdapter.getModel().size) {
                    if (mCartAdapter.getModel()[i].sale_price.isNotEmpty()) {
                        if (mCartAdapter.getModel()[i].sale_price.toFloat() < mCoupons!!.amount.toFloat()) {
                            isValidCoupon = false
                            break
                        }
                    } else {
                        if (mCartAdapter.getModel()[i].price.isNotEmpty()) {
                            if (mCartAdapter.getModel()[i].price.toFloat() < mCoupons!!.amount.toFloat()) {
                                isValidCoupon = false
                            }
                        }
                    }
                }
                if (!isValidCoupon) {
                    txtApplyCouponCode.text =
                        "Coupon is valid only if all product price have " + (mCoupons!!.amount.toFloat() + EXTRA_ADD_AMOUNT) + " and above. Try other coupon."
                    return
                }
            }
            when (mCoupons?.discount_type) {
                "percent" -> {
                    mTotalDiscount =
                        ((mTotalCount.toFloat() * mCoupons!!.amount.toFloat()) / 100).toString()
                    txtDiscountlbl.text =
                        getString(R.string.lbl_discount) + " (" + mCoupons!!.amount + getString(R.string.lbl_off) + ")"
                }
                "fixed_cart" -> {
                    mTotalDiscount = mCoupons!!.amount
                    txtDiscountlbl.text =
                        getString(R.string.lbl_discount) + " (" + getString(R.string.lbl_flat) + mCoupons!!.amount.currencyFormat() + getString(
                            R.string.lbl_off
                        ) + ")"
                }
                "fixed_product" -> {
                    val finalAmout = mCoupons!!.amount.split(".")
                    mTotalDiscount = (finalAmout[0].toInt() * (mOrderItems!!.size)).toString()
                    txtDiscountlbl.text = getString(R.string.lbl_discount)
                }
                else -> {
                    mTotalDiscount = mCoupons!!.amount
                    txtDiscountlbl.text =
                        getString(R.string.lbl_discount) + " (" + getString(R.string.lbl_flat) + mCoupons!!.amount.currencyFormat() + getString(
                            R.string.lbl_off1
                        ) + ")"
                }
            }
            if (mTotalDiscount.toDouble() == 0.0) {
                tvDiscount.text = mTotalDiscount.currencyFormat()
            } else {
                tvDiscount.text = "-" + mTotalDiscount.currencyFormat()
            }
            mTotalCount = mTotalCount.minus(mTotalDiscount.toFloat())

            tvTotalCartAmount.text =
                (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
            txtApplyCouponCode.text =
                getString(R.string.lbl_applied_coupon) + mCoupons!!.code.toUpperCase()
            tvEditCoupon.text = getString(R.string.lbl_remove)

            tvEditCoupon.onClick {
                removeCoupon()
            }
            invalidateShippingMethods()
        }
    }

    private fun removeCoupon() {
        isRemoveCoupons = true
        mCoupons = null
        txtDiscountlbl.text = getString(R.string.lbl_discount)
        txtApplyCouponCode.text = getString(R.string.lbl_coupon_code)
        mTotalCount += mTotalDiscount.toFloat()
        tvEditCoupon.text = getString(R.string.lbl_apply)
        tvDiscount.text = "0".currencyFormat()
        tvTotalCartAmount.text =
            (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
        tvEditCoupon.onClick {
            launchActivity<CouponActivity>(Constants.RequestCode.COUPON_CODE) { }
        }
        invalidateShippingMethods()
    }

    private fun changeColor() {
        lblNoItems.changeTextPrimaryColor()
        btnShopNow.changeBackgroundTint(getButtonColor())
        txtApplyCouponCode.changeTextSecondaryColor()
        tvEditCoupon.changePrimaryColorDark()
        lblShipping.changeTextPrimaryColor()
        tvChange.changePrimaryColorDark()
        tvDiscountMRPTotal.changePrimaryColorDark()
        tvAddress.changeTextSecondaryColor()
        tvFreeShipping.changeTextSecondaryColor()
        lblSubTotal.changeTextSecondaryColor()
        lblDiscountTotal.changeTextSecondaryColor()
        tvTotal.changeTextSecondaryColor()
        txtDiscountlbl.changeTextSecondaryColor()
        tvDiscount.changeTextSecondaryColor()
        txtShippinglbl.changeTextSecondaryColor()
        tvShipping.changeTextSecondaryColor()
        lblTotalAmount.changeTextPrimaryColor()
        tvTotalCartAmount.changeTextPrimaryColor()
        tvContinue.changeBackgroundTint(getButtonColor())
        rlMain.changeBackgroundColor()
    }
}
