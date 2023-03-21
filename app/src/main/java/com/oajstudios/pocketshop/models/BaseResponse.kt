package com.oajstudios.pocketshop.models

import java.io.Serializable

open class BaseResponse(val message: String? = null)
data class UpdateCartResponse(val message: String, val quantity: Int)
data class AddCartResponse(
    val code: String,
    val data: AddCartData,
    val message: String,
)

data class GetStripeClientSecret(
    val client_secret: String,
    val message: String,
)

data class ProfileImage(
    val code: Int = 0,
    val message: String = "",
    val woobox_profile_image: String = "",
)

data class AddCartData(
    val status: Int,
)


data class CheckoutResponse(
    val checkout_url: String,
)

class CreateOrderResponse(

    val id: Int,
    val parent_id: Int,
    val number: Int,
    val order_key: String,
    val created_via: String,
    val version: String,
    val status: String,
    val currency: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val discount_total: Double,
    val discount_tax: Double,
    val shipping_total: Double,
    val shipping_tax: Double,
    val cart_tax: Double,
    val total: Double,
    val total_tax: Double,
    val prices_include_tax: Boolean,
    val customer_id: Int,
    val customer_ip_address: String,
    val customer_user_agent: String,
    val customer_note: String,
    val billing: Billing,
    val shipping: Shipping,
    val payment_method: String,
    val payment_method_title: String,
    val transaction_id: String,
    val date_paid: String,
    val date_paid_gmt: String,
    val date_completed: String,
    val date_completed_gmt: String,
    val cart_hash: String,
    val line_items: List<Line_items>,
    val tax_lines: List<String>,
    val shipping_lines: List<ShippingLines>,
    val fee_lines: List<String>,
    val coupon_lines: List<CouponLines>,
    val refunds: List<String>,
    val currency_symbol: String,
) : Serializable

data class CartResponseModel(
    val data: List<CartResponse>,
    val total_quantity: Int
)

data class CartResponse(
    val cart_id: String,
    val created_at: String,
    val full: String,
    val gallery: List<Any>,
    val name: String,
    val on_sale: Boolean,
    val price: String,
    val pro_id: Int,
    var quantity: String,
    val regular_price: String,
    val sale_price: String,
    val shipping_class: String,
    val shipping_class_id: Int,
    val sku: String,
    val stock_quantity: Any,
    val stock_status: String,
    val thumbnail: String
)


data class RegisterResponse(
    val code: Int,
    val data: Data,
    val message: String,
)

data class Data(
    val first_name: String,
    val last_name: String,
    val user_email: String,
    val user_login: String,
)

data class loginResponse(
    val token: String,
    val user_email: String,
    val user_nicename: String,
    val user_display_name: String,
    val first_name: String,
    val last_name: String,
    val user_id: Int,
    val user_role: List<String>,
    val avatar: String,
    val billing: Billing,
    val shipping: Shipping,
    val woobox_profile_image: String,
)

data class CustomerData(
    // val _links: CustomerLinks,
    val avatar_url: String,
    val billing: Billing,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val is_paying_customer: Boolean,
    val last_name: String,
    // val meta_data: List<MetaData>,
    val role: String,
    val shipping: Shipping,
    val username: String,
)

data class CustomerCollection(
    val href: String,
)

data class CustomerSelf(
    val href: String,
)

class Billing : Serializable {
    var first_name: String = ""
    var last_name: String = ""
    var company: String = ""
    var address_1: String = ""
    var address_2: String = ""
    var city: String = ""
    var postcode: String = ""
    var country: String = ""
    var state: String = ""
    var email: String = ""
    var phone: String = ""
    fun getFullAddress(sap: String = ","): String {
        return "$address_1$sap$address_2$sap$city $postcode$sap$state$sap$country"
    }
}

data class MetaData(
    val id: Int,
    val key: String,
    val value: String,
) : Serializable


class Shipping : Serializable {
    var address_1: String = ""
    var address_2: String = ""
    var city: String = ""
    var country: String = ""
    var first_name: String = ""
    var last_name: String = ""
    var phone: String = ""
    var postcode: String = ""
    var state: String = ""
    var company: String = ""
    fun getFullAddress(sap: String = ","): String {
        return "$address_1$sap$address_2$sap$city $postcode$sap$state$sap$country"
    }
}


data class Order(
    val billing: Billing,
    val cart_hash: String,
    val cart_tax: String,
    // val coupon_lines: List<Any>,
    val created_via: String,
    val currency: String,
    val customer_id: Int,
    val customer_ip_address: String,
    val customer_note: String,
    val customer_user_agent: String,
    val date_completed: Any,
    val date_created: DateCreated,
    val date_modified: DateModified,
    val date_paid: DatePaid,
    val discount_tax: String,
    val discount_total: String,
    val fee_lines: List<Any>,
    val id: Int,
    val line_items: List<LineItems>,
    val meta_data: List<Any>,
    val number: String,
    val order_key: String,
    val parent_id: Int,
    val payment_method: String,
    val payment_method_title: String,
    val prices_include_tax: Boolean,
    val shipping: Shipping,
    //val shipping_lines: List<shipping_lines>,
    val shipping_tax: String,
    val shipping_total: String,
    val status: String,
    val tax_lines: List<Any>,
    val total: String,
    val total_tax: String,
    val transaction_id: String,
    val version: String,
) : Serializable


data class DateCreated(
    val date: String,
    val timezone: String,
    val timezone_type: Int,
) : Serializable

data class DateModified(
    val date: String,
    val timezone: String,
    val timezone_type: Int,
) : Serializable

data class DatePaid(
    val date: String,
    val timezone: String,
    val timezone_type: Int,
) : Serializable

data class LineItems(
    val id: Int,
    val meta_data: List<MetaData>,
    val name: String,
    val order_id: Int,
    val product_id: Int,
    val product_images: List<ProductImage>,
    val quantity: Int,
    val subtotal: String,
    val subtotal_tax: String,
    val tax_class: String,
    val taxes: Taxes,
    val total: String,
    val total_tax: String,
    val variation_id: Int,
) : Serializable


data class ProductImage(
    val alt: String,
    val date_created: String,
    val date_modified: String,
    val id: Int,
    val name: String,
    val position: Int,
    val src: String,
) : Serializable

data class Taxes(
    val subtotal: List<Any>,
    val total: List<Any>,
) : Serializable


data class OrderNotes(
    val _links: Links,
    val author: String,
    val customer_note: Boolean,
    val date_created: String,
    val date_created_gmt: String,
    val id: Int,
    val note: String,
) : Serializable

data class Up(
    val href: String,
)

data class StoreProductAttribute(
    val attribute: List<StoreAttribute>? = null,
    //  val categories: List<Categories>?=null
)

data class StoreAttribute(
    val has_archives: Int,
    val id: String,
    val name: String,
    val order_by: String,
    val slug: String,
    val terms: List<Term>,
    val type: String,
)

data class Term(
    val count: Int = 0,
    val description: String = "",
    val filter: String = "",
    var name: String = "",
    val parent: Int = 0,
    val slug: String = "",
    val taxonomy: String = "",
    val term_group: Int = 0,
    val term_id: Int = 0,
    val term_taxonomy_id: Int = 0,
    var isSelected: Boolean = false,
    var isParent: Boolean = false,
)

data class TokenResponse(
    val code: String,
    val data: TokenData,
)

data class TokenData(
    val status: Int,
)