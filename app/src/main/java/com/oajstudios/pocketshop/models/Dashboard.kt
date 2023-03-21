package com.oajstudios.pocketshop.models

data class Dashboard(
    val social_link: SocialLink,
    val banner: List<DashboardBanner>,
    val slider: List<Slider>,
    val is_dokan_active: Boolean,
    val payment_method: String,
    val theme_color: String,
    val enable_coupons: Boolean,
    val currency_symbol: CurrencySymbol,
    val deal_of_the_day: List<StoreProductModel>,
    val best_selling_product: List<StoreProductModel>,
    val featured: List<StoreProductModel>,
    val newest: List<StoreProductModel>,
    val offer: List<StoreProductModel>,
    val sale_product: List<StoreProductModel>,
    val suggested_for_you: List<StoreProductModel>,
    val app_lang: String,
    val you_may_like: List<StoreProductModel>
)

data class DashboardBanner(
    val desc: String,
    val image: String,
    val thumb: String,
    val url: String
)

data class  Slider(
    val image: String,
    val thumb: String,
    val url: String,
)
data class CurrencySymbol(
    val currency: String,
    val currency_symbol: String
)

data class SocialLink(
    val contact: String,
    val copyright_text: String,
    val facebook: String,
    val instagram: String,
    val privacy_policy: String,
    val term_condition: String,
    val twitter: String,
    val whatsapp: String
)