package com.oajstudios.pocketshop.models

import java.io.Serializable

data class OrderRequest(
    var payment_method: String = "",
    var transaction_id: String = "",
    var customer_id: Int = 0,
    var currency: String = "",
    var status: String = "",
    var set_paid: Boolean = true,
    var billing: Billing? = null,
    var shipping: Shipping? = null,
    var line_items: ArrayList<Line_items>? = null,
    var shipping_lines: ArrayList<ShippingLines>? = null,
    var coupon_lines: ArrayList<CouponLines>? = null
)

data class ShippingLines(
    var method_id: String="",
    var method_title:String="",
    var total: String="",
    var instance_id: String="",
    var total_tax: String="",
    var taxes: ArrayList<Any>? = null,
    var meta_data: ArrayList<Any>? = null
) : Serializable