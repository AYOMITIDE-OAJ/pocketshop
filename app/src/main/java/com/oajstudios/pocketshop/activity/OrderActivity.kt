package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.Order
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.KeyIntent.DATA
import com.oajstudios.pocketshop.utils.Constants.SharedPref.KEY_ORDER_COUNT
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.rlMain
import kotlinx.android.synthetic.main.item_orderlist.view.*
import kotlinx.android.synthetic.main.item_orderlist.view.ivProduct
import kotlinx.android.synthetic.main.item_orderlist.view.tvProductName
import kotlinx.android.synthetic.main.layout_nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.ParseException

class OrderActivity : AppBaseActivity() {

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private val mOrderAdapter =
        BaseAdapter<Order>(R.layout.item_orderlist, onBind = { view, model, _ ->

            if (model.line_items.isNotEmpty()) {
                if (model.line_items[0].product_images[0].src.isNotEmpty()) {
                    view.ivProduct.loadImageFromUrl(model.line_items[0].product_images[0].src)
                }
                if (model.line_items.size > 1) {
                    view.tvProductName.text =
                        model.line_items[0].name + " + " + (model.line_items.size - 1) + " " + getString(
                            R.string.lbl_more_item
                        )
                } else {
                    view.tvProductName.text = model.line_items[0].name
                }
            } else {
                view.tvProductName.text = getString(R.string.lbl_order_title) + model.id
            }

            try {
                if (model.date_paid != null) {
                    if (model.transaction_id.isNullOrBlank()) {
                        view.tvInfo.text =
                            getString(R.string.lbl_transaction_via) + " " + model.payment_method
                    } else {
                        view.tvInfo.text =
                            getString(R.string.lbl_transaction_via) + " " + model.payment_method
                    }
                } else {
                    view.tvInfo.text =
                        getString(R.string.lbl_transaction_via) + " " + model.payment_method
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            view.tvStatus.text = model.status
            view.rlMainOrder.onClick {
                launchActivity<OrderDescriptionActivity>(Constants.RequestCode.ORDER_CANCEL) {
                    putExtra(DATA, model)
                }
            }

            view.tvProductName.changeTextPrimaryColor()
            view.tvInfo.changeTextSecondaryColor()
            view.tvStatus.changeTextPrimaryColor()
            view.tvStatus.changeTextPrimaryColor()
        })

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RequestCode.ORDER_CANCEL) {
                getOrderList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        title = getString(R.string.lbl_my_orders)
        setToolbar(toolbar)
        mAppBarColor()
        rlMain.changeBackgroundColor()
        disableHardwareRendering(rvOrder)
        rvOrder.adapter = mOrderAdapter

        getOrderList()
    }

    private fun getOrderList() {
        getOrder(onApiSuccess = {
            if (it.size == 0) {
                rlNoData.show()
            } else {
                rlNoData.hide()
                mOrderAdapter.clearItems()
                mOrderAdapter.addItems(it)
                getSharedPrefInstance().setValue(KEY_ORDER_COUNT, it.size)
                sendOrderCountChangeBroadcast()
            }
        })
    }

}
