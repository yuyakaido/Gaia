package com.yuyakaido.gaia.core.domain

import android.net.Uri

sealed class Community {
    abstract val id: String
    abstract val name: String

    data class Summary(
        override val id: String,
        override val name: String
    ) : Community()

    data class Detail(
        override val id: String,
        override val name: String,
        val icon: Uri
    ) : Community()

    fun display(): String {
        return "r/$name"
    }
}