package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_orderdescription.*
import kotlinx.android.synthetic.main.activity_orderdescription.rlMain
import kotlinx.android.synthetic.main.dialog_order_cancel.view.*
import kotlinx.android.synthetic.main.item_order.view.*
import kotlinx.android.synthetic.main.item_track.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.ParseException
import kotlin.collections.ArrayList

class OrderDescriptionActivity : AppBaseActivity() {
    private lateinit var orderModel: Order
    private var totalAmt = 0.0
    private var mOrderNoteList = ArrayList<OrderNotes>()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdescription)

        orderModel = intent.getSerializableExtra(Constants.KeyIntent.DATA) as Order

        title =
            getString(R.string.lbl_order_title) + orderModel.id.toString() + " " + getString(R.string.lbl_details)
        setToolbar(toolbar)
        mAppBarColor()
        changeColor()
        val mOrderItemAdapter =
            BaseAdapter<LineItems>(R.layout.item_order) { view, model, _ ->
                view.tvProductName.text = model.name
                view.tvProductName.changeTextPrimaryColor()
                view.tvPrice.changeTextSecondaryColor()
                view.tvOriginalPrice.text = " Qty: " + model.quantity.toString()

                view.tvOriginalPrice.show()
                view.tvOriginalPrice.changeTextSecondaryColor()
                totalAmt += model.subtotal.toFloat()
                if (model.product_images[0].src.isNotEmpty()) {
                    view.ivProduct.loadImageFromUrl(model.product_images[0].src)
                }
                tvTotalPrice.text =
                    DecimalFormat("0.00").format(totalAmt).toString().currencyFormat()
                val value = model.subtotal.toDouble()
                view.tvPrice.text = DecimalFormat("0.00").format(value).toString().currencyFormat()
                view.onClick {
                    if (getProductDetailConstant() == 0) {
                        launchActivity<ProductDetailActivity1> {
                            putExtra(Constants.KeyIntent.PRODUCT_ID, model.product_id)
                        }
                    } else {
                        launchActivity<ProductDetailActivity2> {
                            putExtra(Constants.KeyIntent.PRODUCT_ID, model.product_id)
                        }
                    }
                }
            }


        val mOrderNotesAdapter =
            BaseAdapter<OrderNotes>(R.layout.item_track) { view, model, _ ->
                try {
                    val mValue = model.note.replace("“", "\"").replace("”", "\"")
                    if (isJSONValid(mValue)) {
                        view.tvText1.text = JSONObject(mValue).getString("message")
                        view.tvText2.text = JSONObject(mValue).getString("status")
                    } else {
                        view.tvText1.text = mValue
                        view.tvText2.text = "By admin"
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                }
                view.tvText1.changeTextSecondaryColor()
                view.tvText2.changeTextPrimaryColor()
                view.tvDate.changeTextSecondaryColor()

                view.tvDate.text = convertToLocalDate(model.date_created)
                view.ivLine.setLineColor(Color.parseColor(getPrimaryColorDark()))
                view.ivCircle.setCircleColor(Color.parseColor(getPrimaryColorDark()))
            }
        rvOrderItems.setVerticalLayout()
        rvOrderItems.adapter = mOrderItemAdapter
        mOrderItemAdapter.addItems(orderModel.line_items)
        setDetail()
        rvOrderNotes.setVerticalLayout()
        rvOrderNotes.adapter = mOrderNotesAdapter
        getOrderTracking(orderModel.id, onApiSuccess = {
            if (it.isEmpty()) {
                rlNotes.hide()
            } else {
                it.forEachIndexed { _, orderNotes ->
                    //if (orderNotes.customer_note) {
                    mOrderNoteList.add(orderNotes)
                    // }
                }
                mOrderNotesAdapter.addItems(mOrderNoteList)
            }
        })

        /**
         * Check Order date and display Cancel order button
         *
         */
        if (orderModel.status == Constants.OrderStatus.COMPLETED ||
            orderModel.status == Constants.OrderStatus.REFUNDED ||
            orderModel.status == Constants.OrderStatus.CANCELED ||
            orderModel.status == Constants.OrderStatus.TRASH ||
            orderModel.status == Constants.OrderStatus.FAILED
        ) {
            cancelOrder.hide()
            imgMore.hide()
        } else {
            imgMore.show()
            cancelOrder.show()
        }
        cancelOrder.onClick {
            orderCancelPopup()
        }
    }

    /**
     * Popup for cancel order
     *
     */
    private fun orderCancelPopup() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.lbl_cancel_order)) // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_order_cancel, null)
        val status: ArrayList<String> = ArrayList()
        status.add(getString(R.string.order_cancel_1))
        status.add(getString(R.string.order_cancel_2))
        status.add(getString(R.string.order_cancel_3))
        status.add(getString(R.string.order_cancel_4))
        status.add(getString(R.string.order_cancel_5))
        status.add(getString(R.string.order_cancel_6))
        customLayout.llMainOrder.changeBackgroundColor()

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.spinner_items,
            status
        )
        customLayout.spinner.adapter = adapter
        builder.setView(customLayout) // add a button

        builder.setPositiveButton(
            getString(R.string.lbl_cancel_order)
        ) { dialog, _ ->
            /**
             * Call Api for cancel order
             *
             */
            if (isNetworkAvailable()) {
                val orderRequest = CancelOrderRequest()
                orderRequest.status = "cancelled"
                orderRequest.customer_note = customLayout.spinner.selectedItem.toString()
                showProgress(true)
                getRestApiImpl().cancelOrder(
                    orderModel.id,
                    request = orderRequest,
                    onApiSuccess = {
                        // call Order notes api

                        val notes = CreateOrderNotes()
                        notes.customer_note = true
                        notes.note = "{\n" +
                                "\"status\":\"Cancelled\",\n" +
                                "\"message\":\"Order Canceled by you due to " + customLayout.spinner.selectedItem.toString() + ".\"\n" +
                                "} "
                        getRestApiImpl().addOrderNotes(
                            orderModel.id,
                            request = notes,
                            onApiSuccess = {
                                showProgress(false)
                                setResult(Activity.RESULT_OK)
                                finish()
                            },
                            onApiError = {
                                showProgress(false)
                            })
                        dialog.dismiss()
                    },
                    onApiError = {
                        dialog.dismiss()
                        showProgress(false)
                        snackBarError(it)
                    })
            } else {
                showProgress(false)
                noInternetSnackBar()
                dialog.dismiss()
            }
        }
        builder.setNegativeButton(
            getString(R.string.lbl_close)
        ) { _, _ ->
        }

        builder.show()
    }

    private fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setDetail() {
        try {
            if (orderModel.date_paid != null) {
                if (orderModel.transaction_id.isNullOrBlank()) {
                    tvOrderId.text =
                        getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method + " (" + orderModel.transaction_id + "). " + getString(
                            R.string.lbl_paid_on
                        ) + " " + convertOrderDataToLocalDate(orderModel.date_paid.date)
                } else {
                    tvOrderId.text =
                        getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method + ". " + getString(
                            R.string.lbl_paid_on
                        ) + " " + convertOrderDataToLocalDate(orderModel.date_paid.date)
                }

            } else {
                tvOrderId.text =
                    getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        tvProductName.text = orderModel.line_items[0].name
        tvProductSellerName.text = orderModel.shipping.first_name

        tvPrice.text = orderModel.line_items[0].total.currencyFormat()
        ivProduct.loadImageFromUrl(orderModel.line_items[0].product_images[0].src)

        /**
         * Shipping information
         */
        tvUsername.text = orderModel.shipping.first_name + " " + orderModel.shipping.last_name
        var shippingAddress = ""
        if (orderModel.shipping.address_1.isNotBlank()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shipping.address_1
            } else {
                orderModel.shipping.address_1
            }
        }

        if (orderModel.shipping.address_2.isNotBlank()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shipping.address_2
            } else {
                orderModel.shipping.address_2
            }
        }

        if (orderModel.shipping.city.isNotEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shipping.city
            } else {
                orderModel.shipping.city
            }
        }

        if (orderModel.shipping.postcode.isNotEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + " - " + orderModel.shipping.postcode
            } else {
                orderModel.shipping.postcode
            }
        }

        if (orderModel.shipping.state.isNotEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shipping.state
            } else {
                orderModel.shipping.state
            }
        }


        if (orderModel.shipping.country.isNotEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shipping.country
            } else {
                orderModel.shipping.country
            }
        }

        if (orderModel.shipping.phone.isNotBlank()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + getString(R.string.lbl_phone_number) + orderModel.shipping.phone
            } else {
                orderModel.shipping.phone
            }
        }

        if (orderModel.billing.phone.isNotBlank()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + getString(R.string.lbl_phone_number) + orderModel.billing.phone
            } else {
                orderModel.billing.phone
            }
        }

        tvUserAddress.text = shippingAddress
        tvExtraDiscount.text = orderModel.discount_total.currencyFormat()
        tvShippingFee.text = orderModel.shipping_total.currencyFormat()
        tvTotalAmount.text = orderModel.total.currencyFormat()
    }

    private fun changeColor() {
        cancelOrder.changePrimaryColorDark()
        lblShippingDetail.changeTextPrimaryColor()
        lblItemOrder.changeTextPrimaryColor()
        lblPriceDetail.changeTextPrimaryColor()
        lblExtraDiscount.changeTextSecondaryColor()
        lblShippingFee.changeTextSecondaryColor()
        lblTotalAmount.changeTextPrimaryColor()
        tvOrderId.changeTextSecondaryColor()
        tvProductName.changeTextPrimaryColor()
        tvProductSellerName.changeTextPrimaryColor()
        tvPrice.changeTextPrimaryColor()
        tvUsername.changeTextPrimaryColor()
        tvUserAddress.changeTextSecondaryColor()
        lblAmount.changeTextSecondaryColor()
        tvTotalPrice.changeTextPrimaryColor()
        tvExtraDiscount.changeTextPrimaryColor()
        tvShippingFee.changeTextPrimaryColor()
        tvTotalAmount.changeTextPrimaryColor()
        rlMain.changeBackgroundColor()
        imgMore.changeBackgroundImageTint(getPrimaryColorDark())
    }
}
