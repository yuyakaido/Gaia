package com.yuyakaido.gaia.core.presentation

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yuyakaido.gaia.core.R
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Author
import com.yuyakaido.gaia.core.domain.Community

data class ArticleContent(
    val article: Article,
    val isProcessing: Boolean
)

@Composable
fun ArticleItem(
    content: ArticleContent,
    showDetail: Boolean,
    onClickArticle: (article: Article) -> Unit,
    onClickAuthor: (author: Author) -> Unit,
    onToggleVote: (article: Article) -> Unit,
    onClickShare: (article: Article) -> Unit
) {
    val article = content.article
    val isProcessing = content.isProcessing
    Surface(
        modifier = Modifier.clickable { onClickArticle.invoke(article) }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
        ) {
            CommunityAndAuthor(
                community = article.community,
                author = article.author,
                onClickAuthor = onClickAuthor
            )
            Spacer(modifier = Modifier.size(8.dp))
            ContentAndThumbnail(
                article = article,
                showDetail = showDetail
            )
            ReactionButtons(
                article = article,
                isProcessing = isProcessing,
                onToggleVote = onToggleVote,
                onClickArticle = onClickArticle,
                onClickShare = onClickShare
            )
        }
    }
}

@Composable
fun CommunityAndAuthor(
    community: Community,
    author: Author,
    onClickAuthor: (author: Author) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = community.icon,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                text = community.display(),
                maxLines = 1,
                style = MaterialTheme.typography.caption
            )
            Text(
                text = author.display(),
                modifier = Modifier.clickable { onClickAuthor.invoke(author) },
                maxLines = 1,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun ContentAndThumbnail(
    article: Article,
    showDetail: Boolean
) {
    Row {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (showDetail) {
                    Int.MAX_VALUE
                } else {
                    4
                }
            )
            val body = article.body
            if (body != null) {
                Text(
                    text = article.body,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (showDetail) {
                        Int.MAX_VALUE
                    } else {
                        4
                    }
                )
            }
        }
        if (article.thumbnail != Uri.EMPTY) {
            Spacer(modifier = Modifier.size(8.dp))
            AsyncImage(
                model = article.thumbnail,
                contentDescription = null,
                modifier = Modifier.size(
                    width = 100.dp,
                    height = 80.dp
                ),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ReactionButtons(
    article: Article,
    isProcessing: Boolean,
    onToggleVote: (article: Article) -> Unit,
    onClickArticle: (article: Article) -> Unit,
    onClickShare: (article: Article) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.weight(1f)) {
            TextButton(
                onClick = { onToggleVote.invoke(article) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                if (isProcessing) {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(16.dp)
                                .requiredWidthIn(),
                            strokeWidth = 2.dp
                        )
                    }
                } else if (article.likes == true) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = article.reactions.toString())
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            TextButton(
                onClick = { onClickArticle.invoke(article) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = article.numComments.toString())
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            TextButton(
                onClick = { onClickShare.invoke(article) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(id = R.string.share))
            }
        }
    }
}