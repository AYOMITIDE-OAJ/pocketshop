package com.oajstudios.pocketshop.models

data class CancelOrderRequest(
    var status: String = "",
    var customer_note: String = ""
)

