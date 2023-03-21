package com.oajstudios.pocketshop.models

import java.io.Serializable

data class Dimensions(
        val height: String,
        val length: String,
        val width: String
) : Serializable

data class Links(
        val collection: List<Collection>,
        val self: List<Self>
) : Serializable

data class Self(
        val href: String
) : Serializable

data class Collection(
        val href: String
) : Serializable
