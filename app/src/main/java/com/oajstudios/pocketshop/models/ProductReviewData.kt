package com.oajstudios.pocketshop.models

data class ProductReviewData(
        val _links: ReviewLinks,
        val date_created: String,
        val date_created_gmt: String,
        val id: Int,
        val product_id: Int,
        val rating: Int,
        val review: String,
        val name: String,
        val reviewer_avatar_urls: ReviewerAvatarUrls,
        val email: String,
        val status: String,
        val verified: Boolean,
        var isExist: Boolean=false
)

data class ReviewLinks(
    val collection: List<ReviewCollection>,
    val self: List<ReviewSelf>,
    val up: List<ReviewUp>
)

data class ReviewCollection(
    val href: String
)

data class ReviewSelf(
    val href: String
)

data class ReviewUp(
    val href: String
)

data class ReviewerAvatarUrls(
    val `24`: String,
    val `48`: String,
    val `96`: String
)
