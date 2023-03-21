package com.oajstudios.pocketshop.utils.oauthInterceptor

interface TimestampService {
    val timestampInSeconds: String
    val nonce: String
}