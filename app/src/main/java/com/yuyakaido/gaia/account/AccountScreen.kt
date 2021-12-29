package com.yuyakaido.gaia.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AccountScreen(
    viewModel: AccountViewModel
) {
    when (val state = viewModel.state.value) {
        is AccountViewModel.State.Initial,
        is AccountViewModel.State.Loading,
        is AccountViewModel.State.Error -> {
            Text(text = state::class.java.simpleName)
        }
        is AccountViewModel.State.Ideal -> {
            Column {
                Text(text = state.account.id)
                Text(text = state.account.name)
                Text(text = state.account.icon.toString())
            }
        }
    }
}