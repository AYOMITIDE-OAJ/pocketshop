package com.oajstudios.pocketshop.stripe

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
import com.oajstudios.pocketshop.activity.PaymentSuccessfullyActivity
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import kotlinx.android.synthetic.main.dialog_failed_transaction.*
import kotlinx.android.synthetic.main.payment_stripe_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList


class StripePaymentActivity : AppBaseActivity() {
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe
    private lateinit var paymentId: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_stripe_main)
        title = getString(R.string.lbl_payment_via_stripe)
        setToolbar(toolbar)
        mAppBarColor()
        changeColor()
        PaymentConfiguration.init(
            applicationContext,
            getString(R.string.stripe_publisher_key)
        )
        if (isNetworkAvailable()) {
            // showProgress(true)
            val requestModel = RequestModel()
            requestModel.apiKey = getString(R.string.stripe_secret_key)
            val amount = intent.getStringExtra(Constants.KeyIntent.PRICE)
            val finalAmt = amount!!.split(".")

            txtAmount.text = finalAmt[0].currencyFormat() + " "
            if (checkZeroDecimalCurrencies()) {
                requestModel.amount = finalAmt[0].toInt()
            } else {
                requestModel.amount = finalAmt[0].toInt() * 100
            }
            requestModel.currency = getDefaultCurrencyFormate()
            requestModel.description =
                "Online purchased from " + getString(R.string.app_name) + "(WooBox App) Mobile App "

            getRestApiImpl().getStripeClientSecret(request = requestModel, onApiSuccess = {
                showProgress(false)
                if (it.client_secret.isNotEmpty()) {
                    paymentIntentClientSecret = it.client_secret
                    startCheckout()
                } else {

                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }, onApiError = {
                showProgress(false)
                snackBar(it)
                finish()
            })
        } else {
            noInternetSnackBar()
        }
    }

    private fun startCheckout() {
        tvPay.setOnClickListener {
            showProgress(true)
            val params = cardInputWidget.paymentMethodCreateParams
            if (params != null) {
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                stripe = Stripe(
                    applicationContext,
                    PaymentConfiguration.getInstance(applicationContext).publishableKey
                )
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    Log.e("res", "Payment succeeded")
                    paymentView.hide()
                    createOrderView.show()
                    transactionId.text = paymentIntent.id.toString()
                    paymentId = paymentIntent.id.toString()
                    createOrder()
                } else {
                    Log.e("res", "Payment failed")
                    showPaymentFailDialog()
                }
            }

            override fun onError(e: Exception) {
                showProgress(false)
                showPaymentFailDialog()
                Log.e("res", "Payment failed")
            }
        })
    }

    fun createOrder() {
        val orderRequest = OrderRequest()
        orderRequest.payment_method = "stripe"
        orderRequest.transaction_id = paymentId
        orderRequest.customer_id = getUserId().toInt()
        orderRequest.currency = getDefaultCurrencyFormate()
        orderRequest.status = "pending"
        orderRequest.set_paid = true

        val billing = Billing()
        billing.address_1 = getbillingList().address_1
        billing.address_2 = getbillingList().address_2
        billing.city = getbillingList().city
        billing.country = getbillingList().country
        billing.state = getbillingList().state
        billing.phone = getbillingList().phone
        billing.email = getEmail()
        billing.first_name = getbillingList().first_name
        billing.last_name = getbillingList().last_name

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

        if (intent.getStringExtra(Constants.KeyIntent.COUPON_CODE) != null) {
            val couponCode = intent.getStringExtra(Constants.KeyIntent.COUPON_CODE)
            if (couponCode!!.isNotEmpty()) {
                val couponLines: ArrayList<CouponLines> = ArrayList(1)
                val cop = CouponLines()
                cop.code = couponCode
                couponLines.add(cop)
                orderRequest.coupon_lines = couponLines
            }
        }

        getRestApiImpl().createOrderRequest(request = orderRequest, onApiSuccess = {
            showProgress(false)
            launchActivity<PaymentSuccessfullyActivity> {
                putExtra(Constants.KeyIntent.ORDER_DATA, it)
            }
            getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_USER_CART)
            getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_CART_COUNT)
            setResult(Activity.RESULT_OK)
            this.finish()
        }, onApiError = {
            txtPaymentMessage.text =
                getString(R.string.error_something_went_wrong) + ". " + getString(R.string.lbl_replace_your_order)
            tvOrderAgain.show()
            tvOrderAgain.onClick {
                tvOrderAgain.hide()
                txtPaymentMessage.text = getString(R.string.lbl_we_are_replace_your_order)
                createOrder()
            }
            showProgress(false)
        })
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

    private fun checkZeroDecimalCurrencies(): Boolean {
        val defaultCurrency = getDefaultCurrencyFormate()
        val zeroDecimalCurrencies =
            "MGA, BIF, CLP, DJF, GNF, HUF, JPY, KMF, KRW, XPF, XOF, XAF, VUV, VND, UGX, RWF, PYG"
        if (zeroDecimalCurrencies.contains(defaultCurrency.uppercase(Locale.getDefault()))) {
            return true
        }
        return false
    }

    private fun changeColor() {
        tvPay.changeTint(getAccentColor())
        lblCardDetail.changeTextPrimaryColor()
        lblAmountPayable.changeTextPrimaryColor()
        txtAmount.changeTextPrimaryColor()
        txtPaymentMessage.changeTextPrimaryColor()
        lblTransactionId.changeTextSecondaryColor()
        transactionId.changeTextPrimaryColor()
        tvOrderAgain.changeTint(getAccentColor())
        tvPay.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(getButtonColor()))
    }
}