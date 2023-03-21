package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.stripe.StripePaymentActivity
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.DISCOUNT
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.DISCOUNT_MRP
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.SHIPPINGDATA
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.SUBTOTAL
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.TOTAL
import com.oajstudios.pocketshop.utils.Constants.PAYMENT_METHOD.PAYMENT_METHOD_WEB
import com.oajstudios.pocketshop.utils.extensions.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.activity_order_summary.lblDiscountTotal
import kotlinx.android.synthetic.main.activity_order_summary.lblSubTotal
import kotlinx.android.synthetic.main.activity_order_summary.lblTotalAmount
import kotlinx.android.synthetic.main.activity_order_summary.llMRPDiscount
import kotlinx.android.synthetic.main.activity_order_summary.llShippingAmount
import kotlinx.android.synthetic.main.activity_order_summary.rlMain
import kotlinx.android.synthetic.main.activity_order_summary.tvChange
import kotlinx.android.synthetic.main.activity_order_summary.tvContinue
import kotlinx.android.synthetic.main.activity_order_summary.tvDiscount
import kotlinx.android.synthetic.main.activity_order_summary.tvDiscountMRPTotal
import kotlinx.android.synthetic.main.activity_order_summary.tvShipping
import kotlinx.android.synthetic.main.activity_order_summary.tvTotal
import kotlinx.android.synthetic.main.activity_order_summary.tvTotalCartAmount
import kotlinx.android.synthetic.main.activity_order_summary.txtDiscountlbl
import kotlinx.android.synthetic.main.activity_order_summary.txtShippinglbl
import kotlinx.android.synthetic.main.dialog_failed_transaction.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import org.json.JSONObject
import java.text.DecimalFormat

class OrderSummaryActivity : AppBaseActivity(), PaymentResultListener {

    private val TAG: String = OrderSummaryActivity::class.toString()
    private var isNativePayment = false
    private var shippingItems: Method? = null
    private var mCoupons: Coupons? = null
    private var orderId: Int? = null
    private var mTotalDiscountMRP = 0.0

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        setToolbar(toolbar)
        title = getString(R.string.order_summary)
        mAppBarColor()
        changeColor()
        /**
         * Check payment method type
         */
        isNativePayment = getSharedPrefInstance().getStringValue(
            Constants.SharedPref.PAYMENT_METHOD,
            PAYMENT_METHOD_WEB
        ) != PAYMENT_METHOD_WEB
        shippingItems = intent.getSerializableExtra(SHIPPINGDATA) as Method?
        if (intent.getSerializableExtra(Constants.KeyIntent.COUPON_CODE) != null) {
            mCoupons = intent.getSerializableExtra(Constants.KeyIntent.COUPON_CODE) as Coupons?
        }
        val subtotal = intent.getStringExtra(SUBTOTAL)
        val discount = intent.getStringExtra(DISCOUNT)
        val totalMRP = intent.getStringExtra(TOTAL)
        mTotalDiscountMRP = intent.getDoubleExtra(DISCOUNT_MRP, 0.0)
        if (mTotalDiscountMRP <= 0.0) {
            llMRPDiscount.hide()
        } else {
            tvDiscountMRPTotal.text = "- " + mTotalDiscountMRP.toString().currencyFormat()
            llMRPDiscount.show()
        }
        tvTotal.text = totalMRP?.currencyFormat()

        if (discount != null && discount != "0") {
            llDiscount.show()
            tvDiscount.text = "-" + discount.currencyFormat()
        }
        if (shippingItems != null) {
            llShippingAmount.show()
            if (shippingItems!!.id == "free_shipping") {
                tvShipping.text = "Free"
                tvShipping.setTextColor(color(R.color.green))
            } else {
                if (shippingItems!!.cost.isEmpty()) {
                    shippingItems!!.cost = "0"
                }
                llShippingAmount.show()
                tvShipping.changeTextSecondaryColor()
                tvShipping.text = shippingItems!!.cost.currencyFormat()
            }
        }

        val price = intent.getStringExtra(Constants.KeyIntent.PRICE)
        val precision = DecimalFormat("0.00")
        tvTotalCartAmount.text =
            precision.format(price!!.toDouble()).toString().currencyFormat()

        updateAddress()
        tvContinue.onClick {
            if (isNativePayment) {
                when {
                    rbCard.isChecked -> {
                        launchActivity<StripePaymentActivity>(Constants.RequestCode.STRIPE_PAYMENT) {
                            putExtra(
                                Constants.KeyIntent.COUPON_CODE,
                                intent.getStringExtra(Constants.KeyIntent.COUPON_CODE)
                            )
                            putExtra(
                                Constants.KeyIntent.PRICE,
                                intent.getStringExtra(Constants.KeyIntent.PRICE)
                            )
                            putExtra(
                                Constants.KeyIntent.PRODUCTDATA,
                                intent.getSerializableExtra(Constants.KeyIntent.PRODUCTDATA)
                            )
                        }
                    }
                    rbRazorpay.isChecked -> {
                        showProgress(true)
                        startPayment()
                    }
                    rbCOD.isChecked -> {
                        showProgress(true)
                        val orderRequest = OrderRequest()
                        orderRequest.payment_method = "cod"
                        orderRequest.transaction_id = ""
                        orderRequest.customer_id = getUserId().toInt()
                        orderRequest.currency = getDefaultCurrencyFormate()
                        orderRequest.status = "pending"
                        orderRequest.set_paid = false
                        createOrderRequest(orderRequest)
                    }
                }
            } else {
                showProgress(true)
                val orderRequest = OrderRequest()
                orderRequest.payment_method = ""
                orderRequest.transaction_id = ""
                orderRequest.customer_id = getUserId().toInt()
                orderRequest.currency = getDefaultCurrencyFormate()
                orderRequest.status = "pending"
                orderRequest.set_paid = false
                createOrderRequest(orderRequest)
            }
        }

        if (isNativePayment) {
            paymentView.show()
        } else {
            paymentView.hide()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAddress() {
        val shipping = getShippingList()
        tvUserAddress.text =
            shipping.first_name + " " + shipping.last_name + "\n" + shipping.getFullAddress(sap = "\n")
        val billing = getbillingList()
        tvBillingAddress.text =
            billing.first_name + " " + billing.last_name + "\n" + billing.getFullAddress(sap = "\n")
        if (shippingItems != null) {
            tvMethod.show()
            if (shippingItems!!.id == "free_shipping" || shippingItems!!.cost == "0" || shippingItems!!.cost.isEmpty()) {
                tvMethodName.text = shippingItems!!.methodTitle
            } else {
                tvMethodName.text =
                    shippingItems!!.methodTitle + ": " + shippingItems!!.cost.currencyFormat()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.ADD_ADDRESS -> {
                    updateAddress()
                }
                Constants.RequestCode.STRIPE_PAYMENT -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                Constants.RequestCode.WEB_PAYMENT -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        } else {
            if (requestCode == Constants.RequestCode.WEB_PAYMENT || requestCode == Constants.RequestCode.STRIPE_PAYMENT) {
                deleteOrder()
            }
        }
    }

    private fun deleteOrder() {
        if (orderId != null) {
            getRestApiImpl().deleteOrder(orderId = orderId!!, onApiSuccess = {
            }, onApiError = {
                showProgress(false)
            })
        }
    }

    /**
     * Razorpay Payment
     *
     */
    private fun startPayment() {
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID(getString(R.string.razor_key))
        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", getString(R.string.app_description))
            options.put(
                "image",
                "https://s3.amazonaws.com/rzp-mobile/images/rzp.png"
            )
            options.put("currency", getDefaultCurrencyFormate())
            val orignalPrice = intent.getStringExtra(Constants.KeyIntent.PRICE)
            val finalPrice = orignalPrice!!.toFloat() * 100.00
            options.put("amount", finalPrice.toInt().toString())

            val prefill = JSONObject()
            prefill.put("email", getEmail())
            prefill.put("contact", getbillingList().phone)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            showProgress(false)
            Toast.makeText(
                activity,
                getString(R.string.lbl_error_in_payment) + e.message,
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        try {
            Log.e("onPaymentError", "errorCode:$errorCode \nresponse:$response")
            showPaymentFailDialog()
            showProgress(false)
        } catch (e: Exception) {
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }

    private fun createOrderRequest(orderRequest: OrderRequest) {
        val billing = Billing()
        billing.address_1 = getbillingList().address_1
        billing.address_2 = getbillingList().address_2
        billing.city = getbillingList().city
        billing.country = getbillingList().country
        billing.state = getbillingList().state
        billing.phone = getbillingList().phone
        billing.first_name = getbillingList().first_name
        billing.last_name = getbillingList().last_name
        billing.email = getEmail()

        val shipping = Shipping()
        shipping.address_1 = getShippingList().address_1
        shipping.address_2 = getShippingList().address_2
        shipping.city = getShippingList().city
        shipping.country = getShippingList().country
        shipping.state = getShippingList().state
        shipping.phone = getShippingList().phone
        shipping.first_name = getShippingList().first_name
        shipping.last_name = getShippingList().last_name

        orderRequest.billing = billing
        orderRequest.shipping = shipping

        val orderItems: ArrayList<Line_items> =
            intent.getSerializableExtra(Constants.KeyIntent.PRODUCTDATA) as ArrayList<Line_items>
        orderRequest.line_items = orderItems

        if (shippingItems != null) {
            val itemData = ShippingLines()
            itemData.method_id = shippingItems!!.id
            itemData.method_title = shippingItems!!.methodTitle
            if (shippingItems!!.id == "free_shipping") {
                itemData.total = "0"
            } else {
                if (shippingItems!!.cost.isEmpty()) {
                    shippingItems!!.cost = "0"
                }
                itemData.total = shippingItems!!.cost
            }
            val list = ArrayList<ShippingLines>()
            list.add(itemData)
            orderRequest.shipping_lines = list
        }

        if (mCoupons != null) {
            val couponLines: ArrayList<CouponLines> = ArrayList(1)
            val cop = CouponLines()
            cop.code = mCoupons!!.code
            couponLines.add(cop)
            orderRequest.coupon_lines = couponLines
        }

        getRestApiImpl().createOrderRequest(request = orderRequest, onApiSuccess = {
            orderId = it.id
            if (isNativePayment) {
                showProgress(false)
                launchActivity<PaymentSuccessfullyActivity>(Constants.RequestCode.ROZORPAY_PAYMENT) {
                    putExtra(Constants.KeyIntent.ORDER_DATA, it)
                }
                getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_USER_CART)
                getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_CART_COUNT)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                getCheckoutUrl(it.id)
            }
        }, onApiError = {
            showProgress(false)
        })
    }

    private fun getCheckoutUrl(id: Int) {
        val request = CheckoutUrlRequest()
        request.order_id = id.toString()
        getRestApiImpl().getCheckoutUrl(request = request, onApiSuccess = {
            showProgress(false)
            launchActivity<WebViewActivity>(Constants.RequestCode.WEB_PAYMENT) {
                putExtra(Constants.KeyIntent.CHECKOUT_URL, it.checkout_url)
            }
        }, onApiError = {
            showProgress(false)
        })
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Log.e(TAG, " onPaymentSuccess")
        try {
            /**
             * Create Order request for successfully payment
             *
             */
            val orderRequest = OrderRequest()
            orderRequest.payment_method = "razorpay"
            orderRequest.transaction_id = razorpayPaymentId.toString()
            orderRequest.customer_id = getUserId().toInt()
            orderRequest.currency = getDefaultCurrencyFormate()
            orderRequest.status = "pending"
            orderRequest.set_paid = true

            createOrderRequest(orderRequest)
        } catch (e: Exception) {
            showProgress(false)
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }

    private fun showPaymentFailDialog() {
        val changePasswordDialog = Dialog(this)
        changePasswordDialog.setCancelable(false)
        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        changePasswordDialog.setContentView(R.layout.dialog_failed_transaction)
        changePasswordDialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        changePasswordDialog.tv_close.onClick {
            changePasswordDialog.dismiss()
            finish()
        }
        changePasswordDialog.show()
    }

    private fun changeColor() {
        tvContinue.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getButtonColor()))
        tvAddressHeading.changeTextPrimaryColor()
        tvUserAddress.changeTextSecondaryColor()
        tvMethod.changeTextSecondaryColor()
        tvMethodName.changeTextSecondaryColor()
        tvChange.changePrimaryColorDark()
        lblBillingAddress.changeTextPrimaryColor()
        tvBillingAddress.changeTextSecondaryColor()
        lblPaymentMethod.changeTextPrimaryColor()
        rbCard.changeTextSecondaryColor()
        rbRazorpay.changeTextSecondaryColor()
        rbCOD.changeTextSecondaryColor()
        lblSubTotal.changeTextSecondaryColor()
        lblDiscountTotal.changeTextSecondaryColor()
        tvTotal.changeTextSecondaryColor()
        tvDiscountMRPTotal.changePrimaryColorDark()
        txtDiscountlbl.changeTextSecondaryColor()
        tvDiscount.changeTextSecondaryColor()
        txtShippinglbl.changeTextSecondaryColor()
        tvShipping.changeTextSecondaryColor()
        lblTotalAmount.changeTextPrimaryColor()
        tvTotalCartAmount.changeTextPrimaryColor()
        rlMain.changeBackgroundColor()
    }
}