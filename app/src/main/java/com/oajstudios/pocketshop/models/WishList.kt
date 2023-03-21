package com.oajstudios.pocketshop.models

data class WishList(
    val created_at: String,
    val full: String,
    val gallery: List<String>,
    val name: String,
    val price: String,
    val pro_id: Int,
    val regular_price: String,
    val sale_price: String,
    val sku: String,
    val stock_quantity: Any,
    val thumbnail: String,
)
