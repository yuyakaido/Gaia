package com.yuyakaido.gaia.account.presentation

import androidx.annotation.StringRes
import com.yuyakaido.gaia.core.R

enum class AccountTab(
    @StringRes val title: Int
) {
    Post(title = R.string.account_tab_post),
    Comment(title = R.string.account_tab_comment),
    About(title = R.string.account_tab_about)
}