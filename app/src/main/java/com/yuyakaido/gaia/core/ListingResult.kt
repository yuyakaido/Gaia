package com.yuyakaido.gaia.core

data class ListingResult<T>(
    val items: List<T>,
    val before: String?,
    val after: String?
)