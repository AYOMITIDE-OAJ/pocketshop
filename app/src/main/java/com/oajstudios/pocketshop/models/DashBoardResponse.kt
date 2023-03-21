package com.oajstudios.pocketshop.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DashBoardResponse {
    @SerializedName("dashboard")
     val dashboard: BuilderDashboard? = null

    @SerializedName("productdetailview")
     val Productdetailview: BuilderDetail? = null

    @SerializedName("appsetup")
     val appSetup: AppSetup? = null

}
open class AppSetup {
    @SerializedName("appName")
    var appName: String? = null

    @SerializedName("primaryColor")
    var primaryColor: String? = null

    @SerializedName("secondaryColor")
    var secondaryColor: String? = null

    @SerializedName("textPrimaryColor")
    var textPrimaryColor: String? = null

    @SerializedName("textSecondaryColor")
    var textSecondaryColor: String? = null

    @SerializedName("backgroundColor")
    var backgroundColor: String? = null

    @SerializedName("consumerKey")
    var consumerKey: String? = null

    @SerializedName("consumerSecret")
    var consumerSecret: String? = null

    @SerializedName("appUrl")
    var appUrl: String? = null

}
class AppBar {
    @SerializedName("title")
    var title: String=""

    @SerializedName("appBarIcon")
    var appBarIcon: String=""

    @SerializedName("backgroundColor")
    var backgroundColor: String=""

    @SerializedName("appBarLayout")
    var appBarLayout: String=""

}

open class BuilderDashboard : Serializable {
    @SerializedName("layout")
    var layout: String=""

    @SerializedName("sorting")
    var sorting: List<String>? = null

    @SerializedName("backgroundColor")
    var backgroundColor: String? = null

    @SerializedName("appBars")
    var appBars: AppBar? = null

    @SerializedName("sliderView")
    var sliderView: SliderView? = null

    @SerializedName("newProduct")
    var newProduct: Product? = null

    @SerializedName("feature")
    var feature: Product? = null

    @SerializedName("saleProduct")
    var saleProduct: Product? = null

    @SerializedName("dealOfTheDay")
    var dealOfTheDay: Product? = null

    @SerializedName("bestSaleProduct")
    var bestSaleProduct: Product? = null

    @SerializedName("offerProduct")
    var offerProduct: Product? = null

    @SerializedName("suggestionProduct")
     var suggestionProduct: Product? = null

    @SerializedName("youMayLikeProduct")
    var youMayLikeProduct: Product? = null


}
open class Product : Serializable {
    @SerializedName("enable")
    var enable: Boolean? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("viewAll")
    var viewAll: String? = null
}

open class SliderView : Serializable  {
    @SerializedName("enable")
    var enable: Boolean? = null

}

open class BuilderDetail : Serializable {
    @SerializedName("layout")
    var layout: String? = null

}
