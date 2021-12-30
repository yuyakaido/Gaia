package com.yuyakaido.gaia.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuyakaido.gaia.app.Screen
import com.yuyakaido.gaia.domain.Message
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Composable
fun MessageListScreen(
    viewModel: MessageListViewModel
) {
    viewModel.state.value.let {
        when (it) {
            is MessageListViewModel.State.Initial,
            is MessageListViewModel.State.Loading,
            is MessageListViewModel.State.Error -> {
                Text(text = it::class.java.simpleName)
            }
            is MessageListViewModel.State.Ideal -> {
                MessageList(messages = it.messages)
            }
        }
    }
}

@ExperimentalSerializationApi
@Composable
fun MessageList(
    messages: List<Message>
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        )
    ) {
        items(messages) {
            MessageItem(message = it)
        }
    }
}

@Composable
fun MessageItem(
    message: Message
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = message.author)
        Text(
            text = message.subject,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = message.body,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}