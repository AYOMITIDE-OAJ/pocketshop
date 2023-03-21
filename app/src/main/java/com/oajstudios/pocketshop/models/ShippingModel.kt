package com.oajstudios.pocketshop.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ShippingModel {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("methods")
    @Expose
    var methods: ArrayList<Method>? = null

    @SerializedName("headers")
    @Expose
    var headers: ArrayList<Any>? = null
}

class InstanceSettings {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("tax_status")
    @Expose
    var taxStatus: String? = null

    @SerializedName("cost")
    @Expose
    var cost: String? = null

    @SerializedName("class_costs")
    @Expose
    var classCosts: String? = null

    @SerializedName("class_cost_35")
    @Expose
    var classCost35: String? = null

    @SerializedName("class_cost_36")
    @Expose
    var classCost36: String? = null

    @SerializedName("no_class_cost")
    @Expose
    var noClassCost: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("ignore_discounts")
    @Expose
    var ignore_discounts: String? = null


}

class Settings {
    @SerializedName("class_cost_flate")
    @Expose
    var classCostFlate: String? = null

    @SerializedName("class_cost_free-shipping")
    @Expose
    var classCostFreeShipping: String? = null

}

class Method : Serializable {
    @SerializedName("min_amount")
    @Expose
    var minAmount: String = ""

    @SerializedName("requires")
    @Expose
    var requires: String = ""

    @SerializedName("supports")
    @Expose
    var supports: List<String>? = null

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("method_title")
    @Expose
    var methodTitle: String = ""

    @SerializedName("method_description")
    @Expose
    var methodDescription: String = ""

    @SerializedName("enabled")
    @Expose
    var enabled: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("rates")
    @Expose
    var rates: List<Any>? = null

    @SerializedName("tax_status")
    @Expose
    var taxStatus: String = ""

    @SerializedName("fee")
    @Expose
    var fee: Any? = null

    @SerializedName("minimum_fee")
    @Expose
    var minimumFee: Any? = null

    @SerializedName("instance_id")
    @Expose
    var instanceId: Int? = null

    @SerializedName("availability")
    @Expose
    var availability: Any? = null

    @SerializedName("countries")
    @Expose
    var countries: List<Any>? = null

    @SerializedName("plugin_id")
    @Expose
    var pluginId: String = ""

    @SerializedName("errors")
    @Expose
    var errors: List<Any>? = null

    @SerializedName("settings")
    @Expose
    var settings: List<Any>? = null

    @SerializedName("form_fields")
    @Expose
    var formFields: List<Any>? = null

    @SerializedName("data")
    @Expose
    var data: List<Any>? = null

    @SerializedName("ignore_discounts")
    @Expose
    var ignoreDiscounts: String = ""

    @SerializedName("method_order")
    @Expose
    var methodOrder: Int? = null

    @SerializedName("has_settings")
    @Expose
    var hasSettings: Boolean? = null

    @SerializedName("settings_html")
    @Expose
    var settingsHtml: String = ""

    @SerializedName("cost")
    @Expose
    var cost: String = ""

    var isSelected: Boolean = false


}