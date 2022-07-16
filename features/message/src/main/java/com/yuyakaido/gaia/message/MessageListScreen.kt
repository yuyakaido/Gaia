package com.yuyakaido.gaia.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuyakaido.gaia.core.domain.Message

@Composable
fun MessageListScreen(
    viewModel: MessageListViewModel,
    onMessageClicked: (message: Message) -> Unit
) {
    viewModel.state.value.let {
        when (it) {
            is MessageListViewModel.State.Initial,
            is MessageListViewModel.State.Loading,
            is MessageListViewModel.State.Error -> {
                Text(text = it::class.java.simpleName)
            }
            is MessageListViewModel.State.Ideal -> {
                MessageList(
                    messages = it.messages,
                    onMessageClicked = onMessageClicked
                )
            }
        }
    }
}

@Composable
fun MessageList(
    messages: List<Message>,
    onMessageClicked: (message: Message) -> Unit
) {
    LazyColumn {
        items(messages) {
            MessageItem(
                message = it,
                onMessageClicked = onMessageClicked
            )
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
    onMessageClicked: (message: Message) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMessageClicked.invoke(message)
            }
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {
            Image(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = message.subject,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = message.body,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = "u/${message.author}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}