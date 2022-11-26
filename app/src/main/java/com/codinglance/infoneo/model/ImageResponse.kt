package com.codinglance.infoneo.model

data class ImageResponse(
    val `data`: String,
    val domain: String,
    val fullurl: String,
    val status: String,
    val statuscode: Int
)