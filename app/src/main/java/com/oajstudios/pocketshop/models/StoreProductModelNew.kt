package com.oajstudios.pocketshop.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class StoreProductModelNew {
    @SerializedName("num_of_pages")
    @Expose
    var numOfPages: Int = 0

    @SerializedName("data")
    @Expose
    var data: ArrayList<StoreProductModel>? = null

}