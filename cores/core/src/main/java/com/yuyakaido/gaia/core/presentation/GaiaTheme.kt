package com.yuyakaido.gaia.core.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun GaiaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = lightColors(
            background = Color.LightGray
        ),
        content = content
    )
}