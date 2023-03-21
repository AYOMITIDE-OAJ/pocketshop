package com.oajstudios.pocketshop.utils

import java.text.SimpleDateFormat
import java.util.*

object Constants {

    val FULL_DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    val DD_MMM_YYYY = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    val YYYY_MM_DD = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    const val ONE_SIGNAL_APP_ID = "bb8ad8c0-00fc-4999-b302-9aa5c99a594a";

    const val EXTRA_ADD_AMOUNT = 55

    const val myPreferences = "MyPreferences"

    object RequestCode {
        const val ADD_ADDRESS = 201
        const val COUPON_CODE = 202
        const val ACCOUNT = 203
        const val ROZORPAY_PAYMENT = 204
        const val ORDER_SUMMARY = 205
        const val ORDER_CANCEL = 206
        const val STRIPE_PAYMENT = 207
        const val WEB_PAYMENT = 208
        const val EDIT_PROFILE = 209
        const val WISHLIST = 210
        const val LOGIN = 211
        const val SIGN_IN = 206
        const val SETTINGS = 212


    }

    object SharedPref {
        const val IS_LOGGED_IN = "isLoggedIn"
        const val USER_ID = "user_id"
        const val USER_DISPLAY_NAME = "user_display_name"
        const val USER_NICE_NAME = "user_nicename"
        const val USER_EMAIL = "user_email"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_COUNTRY_CODE = "user_country_code"
        const val USER_PICODE = "user_pincode"
        const val USER_STATE_CODE = "user_state_code"
        const val USER_STATE_NAME = "user_state_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_PASSWORD = "user_password"
        const val USER_USERNAME = "user_username"
        const val USER_ROLE = "user_username"
        const val SHOW_SWIPE = "showswipe"
        const val KEY_USER_ADDRESS = "user_address"
        const val KEY_USER_CART = "user_cart"
        const val KEY_WISHLIST_COUNT = "wishlist_count"
        const val KEY_ORDER_COUNT = "order_count"
        const val KEY_CART_COUNT = "cart_count"
        const val USER_PROFILE = "user_profile"
        const val COUNTRY = "COUNTRY"
        const val BILLING = "user_billing"
        const val SHIPPING = "user_shipping"
        const val USER_TOKEN = "user_token"
        const val THEME_COLOR = "theme_color"
        const val DEFAULT_CURRENCY = "default_currency"
        const val DEFAULT_CURRENCY_FORMATE = "default_currency_formate"
        const val ENABLE_COUPONS = "enable_coupons"
        const val WHATSAPP = "whatsapp"
        const val FACEBOOK = "facebook"
        const val TWITTER = "twitter"
        const val INSTAGRAM = "instagram"
        const val CONTACT = "contact"
        const val PRIVACY_POLICY = "privacy_policy"
        const val TERM_CONDITION = "term_condition"
        const val COPYRIGHT_TEXT = "copyright_text"
        const val LANGUAGE = "selected_language"
        const val IS_SOCIAL_LOGIN = "is_social_login"
        const val PAYMENT_METHOD = "payment_method"
        const val THEME = "selected_theme"
        const val KEY_DASHBOARD = "key_dashboard"
        const val KEY_PRODUCT_DETAIL = "key_product_detail"
        const val CONSUMERKEY = "consumerKey"
        const val CONSUMERSECRET = "consumerSecret"
        const val PRIMARYCOLOR = "primaryColor"
        const val PRIMARYCOLORDARK = "primaryColorDark"
        const val ACCENTCOLOR = "accentColor"
        const val TEXTPRIMARYCOLOR = "textPrimaryColor"
        const val TEXTSECONDARYCOLOR = "textSecondaryColor"
        const val BACKGROUNDCOLOR = "backgroundColor"
        const val DASHBOARDDATA = "dashboardData"
        const val MODE = "mode"
        const val APPURL = "appurl"
        //const val IS_TOKEN_EXPIRED = "IS_TOKEN_EXPIRED";
    }

    object KeyIntent {
        const val PRODUCT_ID: String = "product_id"
        const val SHOW_PAGINATION = "show_pagination"
        const val TITLE = "title"
        const val ADDRESS_ID = "address_id"
        const val KEYID = "key_id"
        const val DATA = "data"
        const val CHECKOUT_URL = "checkoutUrl"
        const val EXTERNAL_URL = "external_url"
        const val COUPON_CODE = "CouponCode"
        const val PRICE = "price"
        const val PRODUCTDATA = "productdata"
        const val SHIPPINGDATA = "shippingdata"
        const val SUBTOTAL="SUBTOTAL"
        const val TOTAL="TOTAL"
        const val DISCOUNT="DISCOUNT"
        const val SHIPPING="SHIPPING"
        const val DISCOUNT_MRP="DISCOUNT_MRP"
        const val ORDER_DATA = "orderData"
        const val VIEWALLID = "viewallid"
        const val SPECIAL_PRODUCT_KEY = "special_product_key"
        const val IS_BANNER = "is_banner"

    }

    object OrderStatus {
        const val COMPLETED = "completed"
        const val REFUNDED = "refunded"
        const val CANCELED = "cancelled"
        const val TRASH = "trash"
        const val FAILED = "failed"
    }

    object viewAllCode {
        const val NEWEST = 102
        const val FEATURED = 103
        const val OTHER = 105
        const val SALE = 106
        const val CATEGORY = 104
        const val BESTSELLING = 107
        const val SPECIAL_PRODUCT = 108
    }

    object AppBroadcasts {
        const val CART_COUNT_CHANGE = "app.broadcast.setCartCount"
        const val ORDER_COUNT_CHANGE = "app.broadcast.OnOrderCountChanged"
        const val PROFILE_UPDATE = "app.broadcast.OnProfileUpdated"
        const val WISHLIST_UPDATE = "app.broadcast.OnWishListUpdated"
        const val CARTITEM_UPDATE = "app.broadcast.OnCartItemUpdated"
    }

    object DateFormatCodes {
        const val YMD_HMS = 0
        const val YMD = 1

    }

    object PAYMENT_METHOD {
        const val PAYMENT_METHOD_NATIVE = "native"
        const val PAYMENT_METHOD_WEB = "webview"
    }

    object THEME {
        const val DARK = 1
        const val LIGHT = 2
    }

    object TotalItem
    {
        const val TOTAL_ITEM_PER_PAGE=20
        const val TOTAL_CATEGORY_PER_PAGE=20
        const val TOTAL_SUB_CATEGORY_PER_PAGE=50
    }

    object viewName {
        const val VIEW_BANNER = "slider"
        const val VIEW_NEWEST = "newest_product"
        const val VIEW_FEATURED = "feature_products"
        const val VIEW_DEAL_OF_THE_DAY = "deal_of_the_day"
        const val VIEW_BEST_SELLING = "best_selling_product"
        const val VIEW_SALE = "sale_product"
        const val VIEW_OFFER = "offer"
        const val VIEW_SUGGESTED_FOR_YOU = "suggested_for_you"
        const val VIEW_YOU_MAY_LIKE = "you_may_like"
    }
}
