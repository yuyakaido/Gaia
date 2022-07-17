package com.yuyakaido.gaia.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuyakaido.gaia.R
import com.yuyakaido.gaia.core.domain.Session

@Composable
fun SessionList(
    sessions: List<Session>,
    addNewSession: () -> Unit,
    activateSession: (session: Session) -> Unit
) {
    Column {
        LazyColumn {
            items(sessions) {
                Row(
                    modifier = Modifier
                        .height(height = 50.dp)
                        .fillMaxSize()
                        .clickable { activateSession.invoke(it) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (it.isActive) {
                            "\uD83D\uDD35 ${it.name}"
                        } else {
                            it.name
                        },
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(height = 50.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { addNewSession.invoke() }
            ) {
                Text(text = stringResource(id = R.string.add_new_session))
            }
        }
    }
}