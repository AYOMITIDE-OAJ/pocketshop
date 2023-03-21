package com.oajstudios.pocketshop.models

import java.io.Serializable

data class Coupons(
    val _links: CouponsLinks,
    val amount: String,
    val code: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_expires: String,
    val date_expires_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val description: String,
    val discount_type: String,
    val email_restrictions: List<Any>,
    val exclude_sale_items: Boolean,
    val excluded_product_categories: List<Any>,
    val excluded_product_ids: List<Any>,
    val free_shipping: Boolean,
    val id: Int,
    val individual_use: Boolean,
    val limit_usage_to_x_items: Any,
    val maximum_amount: String,
    val meta_data: List<Any>,
    val minimum_amount: String,
    val product_categories: List<Any>,
    val product_ids: List<Any>,
    val usage_count: Int = 0,
    val usage_limit: Int = 0,
    val usage_limit_per_user: Any,
    val used_by: List<Any>
) : Serializable

data class CouponsLinks(
    val collection: List<CouponsCollection>,
    val self: List<CouponsSelf>
) : Serializable

data class CouponsCollection(
    val href: String
) : Serializable

data class CouponsSelf(
    val href: String
) : Serializable