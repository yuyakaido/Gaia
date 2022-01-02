package com.yuyakaido.gaia.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
            val account = state.account
            Row(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberImagePainter(
                        data = account.icon
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        text = "u/${account.name}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = "${account.totalKarma} karma",
                        fontSize = 20.sp
                    )
                    Text(
                        text = account.createdAt.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.MEDIUM // Nov 2, 2019
                            )
                        ),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}