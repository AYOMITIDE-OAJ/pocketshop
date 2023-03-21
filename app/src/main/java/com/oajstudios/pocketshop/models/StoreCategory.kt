package com.oajstudios.pocketshop.models

data class Category(
    val _links: CategoryLinks,
    val count: Int,
    val description: String,
    val display: String,
    val id: Int,
    val image: CategoryImage?= null,
    val menu_order: Int,
    val name: String,
    val parent: Int,
    val slug: String
)

data class CategoryLinks(
    val collection: List<CategoryCollection>,
    val self: List<CategorySelf>
)

data class CategoryCollection(
    val href: String
)

data class CategorySelf(
    val href: String
)

data class CategoryImage(
    val alt: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val id: Int,
    val name: String,
    val src: String
)