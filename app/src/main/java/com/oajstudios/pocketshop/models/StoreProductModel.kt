package com.oajstudios.pocketshop.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoreProductModel : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

    @SerializedName("permalink")
    @Expose
    var permalink: String? = null

    @SerializedName("date_created")
    @Expose
    var dateCreated: String? = null

    @SerializedName("date_created_gmt")
    @Expose
    var dateCreatedGmt: String? = null

    @SerializedName("date_modified")
    @Expose
    var dateModified: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("is_added_cart")
    @Expose
    var is_added_cart: Boolean = false

    @SerializedName("is_added_wishlist")
    @Expose
    var is_added_wishlist: Boolean = false

    @SerializedName("store")
    @Expose
    var store: Store? = null

    @SerializedName("woofv_video_embed")
    @Expose
    var woofv_video_embed: WooBoxVideoEmbed? = null

    @SerializedName("upsell_id")
    @Expose
    var upsell_id: List<StoreUpSale>? = null

    @SerializedName("date_modified_gmt")
    @Expose
    var dateModifiedGmt: String? = null

    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null

    @SerializedName("catalog_visibility")
    @Expose
    var catalogVisibility: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("short_description")
    @Expose
    var shortDescription: String? = null

    @SerializedName("sku")
    @Expose
    var sku: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("regular_price")
    @Expose
    var regularPrice: String? = null

    @SerializedName("sale_price")
    @Expose
    var salePrice: String? = null

    @SerializedName("date_on_sale_from")
    @Expose
    var dateOnSaleFrom: Any? = null

    @SerializedName("date_on_sale_from_gmt")
    @Expose
    var dateOnSaleFromGmt: Any? = null

    @SerializedName("date_on_sale_to")
    @Expose
    var dateOnSaleTo: Any? = null

    @SerializedName("date_on_sale_to_gmt")
    @Expose
    var dateOnSaleToGmt: Any? = null

    @SerializedName("price_html")
    @Expose
    var priceHtml: String? = null

    @SerializedName("on_sale")
    @Expose
    var onSale: Boolean = false

    @SerializedName("purchasable")
    @Expose
    var purchasable: Boolean = false

    @SerializedName("total_sales")
    @Expose
    var totalSales: Int? = null

    @SerializedName("virtual")
    @Expose
    var virtual: Boolean? = null

    @SerializedName("downloadable")
    @Expose
    var downloadable: Boolean? = null

    @SerializedName("downloads")
    @Expose
    var downloads: List<Any>? = null

    @SerializedName("download_limit")
    @Expose
    var downloadLimit: Int? = null

    @SerializedName("download_expiry")
    @Expose
    var downloadExpiry: Int? = null

    @SerializedName("external_url")
    @Expose
    var externalUrl: String? = null

    @SerializedName("button_text")
    @Expose
    var buttonText: String? = null

    @SerializedName("tax_status")
    @Expose
    var taxStatus: String? = null

    @SerializedName("tax_class")
    @Expose
    var taxClass: String? = null

    @SerializedName("manage_stock")
    @Expose
    var manageStock: Boolean? = null

    @SerializedName("stock_quantity")
    @Expose
    var stockQuantity: Int? = null

    @SerializedName("stock_status")
    @Expose
    var stockStatus: String? = null

    @SerializedName("backorders")
    @Expose
    var backorders: String? = null

    @SerializedName("backorders_allowed")
    @Expose
    var backordersAllowed: Boolean? = null

    @SerializedName("backordered")
    @Expose
    var backordered: Boolean? = null

    @SerializedName("sold_individually")
    @Expose
    var soldIndividually: Boolean? = null

    @SerializedName("weight")
    @Expose
    var weight: String? = null

    @SerializedName("dimensions")
    @Expose
    var dimensions: Dimensions? = null

    @SerializedName("shipping_requiredrating_count")
    @Expose
    var shippingRequired: Boolean? = null

    @SerializedName("shipping_taxable")
    @Expose
    var shippingTaxable: Boolean? = null

    @SerializedName("shipping_class")
    @Expose
    var shippingClass: String? = null

    @SerializedName("shipping_class_id")
    @Expose
    var shippingClassId: Int? = null

    @SerializedName("reviews_allowed")
    @Expose
    var reviewsAllowed: Boolean? = null

    @SerializedName("average_rating")
    @Expose
    var averageRating: String? = null

    @SerializedName("rating_count")
    @Expose
    var ratingCount: Int? = null

    @SerializedName("related_ids")
    @Expose
    var relatedIds: List<Int>? = null

    @SerializedName("upsell_ids")
    @Expose
    var upsellIds: List<Int>? = null

    @SerializedName("cross_sell_ids")
    @Expose
    var crossSellIds: List<Any>? = null

    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null

    @SerializedName("purchase_note")
    @Expose
    var purchaseNote: String? = null

    @SerializedName("categories")
    @Expose
    var categories: List<StoreCategory>? = null

    @SerializedName("tags")
    @Expose
    var tags: List<Any>? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? = null

    @SerializedName("attributes")
    @Expose
    var attributes: List<Attributes>? = null

    @SerializedName("default_attributes")
    @Expose
    var defaultAttributes: List<Any>? = null

    @SerializedName("variations")
    @Expose
    var variations: List<Int>? = null

    @SerializedName("grouped_products")
    @Expose
    var groupedProducts: List<Any>? = null

    @SerializedName("menu_order")
    @Expose
    var menuOrder: Int? = null

    @SerializedName("meta_data")
    @Expose
    var metaData: List<Any>? = null

    @SerializedName("_links")
    @Expose
    var links: Links? = null

    @SerializedName("in_stock")
    @Expose
    var in_stock: Boolean = false
}

class WooBoxVideoEmbed : Serializable {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("poster")
    @Expose
    var poster: String? = null

    @SerializedName("autoplay")
    @Expose
    var autoplay: Boolean = false

}

class Store : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("shop_name")
    @Expose
    var shop_name: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("address")
    @Expose
    var address: Address? = null

    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("store_open_close")
    @Expose
    var store_open_close: StoreClose? = null
}

class StoreClose : Serializable {
    @SerializedName("enabled")
    @Expose
    var enabled: Boolean = false

    @SerializedName("time")
    @Expose
    var time: Any = false

    @SerializedName("open_notice")
    @Expose
    var open_notice: String? = null

    @SerializedName("close_notice")
    @Expose
    var close_notice: String? = null
}

class Address : Serializable {
    @SerializedName("street_1")
    @Expose
    var street_1: String? = null

    @SerializedName("street_2")
    @Expose
    var street_2: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("zip")
    @Expose
    var zip: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

}

class Image : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("date_created")
    @Expose
    var dateCreated: String? = null

    @SerializedName("date_created_gmt")
    @Expose
    var dateCreatedGmt: String? = null

    @SerializedName("date_modified")
    @Expose
    var dateModified: String? = null

    @SerializedName("date_modified_gmt")
    @Expose
    var dateModifiedGmt: String? = null

    @SerializedName("src")
    @Expose
    var src: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("alt")
    @Expose
    var alt: String? = null

}

class Attributes : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("position")
    @Expose
    var position: Int? = null

    @SerializedName("visible")
    @Expose
    var visible: Boolean? = null

    @SerializedName("variation")
    @Expose
    var variation: Boolean? = null

    @SerializedName("options")
    @Expose
    var options: List<String>? = null

    @SerializedName("option")
    @Expose
    var optionsString: String? = null

}

class StoreCategory : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

}

class StoreCollection : Serializable {

    @SerializedName("href")
    @Expose
    var href: String? = null

}

class StoreUpSale : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("slug")
    @Expose
    var slug: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("regular_price")
    @Expose
    var regular_price: String? = null

    @SerializedName("sale_price")
    @Expose
    var sale_price: String? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? = null
}