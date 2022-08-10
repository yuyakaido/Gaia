package com.yuyakaido.gaia.account.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AccountScreen(
    viewModel: AccountViewModel
) {
    when (val s = viewModel.state.collectAsState().value) {
        is AccountViewModel.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AccountViewModel.State.Ideal -> {
            val account = s.account
            Column {
                Row(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = account.icon,
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
                TabRow(
                    selectedTabIndex = s.selectedTab.ordinal,
                    modifier = Modifier.height(48.dp),
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    AccountTab.values().forEach {
                        Tab(
                            selected = it == s.selectedTab,
                            onClick = { viewModel.onSelectTab(it) }
                        ) {
                            Text(text = stringResource(id = it.title))
                        }
                    }
                }
            }
        }
    }
}