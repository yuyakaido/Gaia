package com.yuyakaido.gaia.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.yuyakaido.gaia.account.R
import com.yuyakaido.gaia.core.domain.Account
import com.yuyakaido.gaia.core.domain.Article
import com.yuyakaido.gaia.core.domain.Comment
import com.yuyakaido.gaia.core.domain.Trophy
import com.yuyakaido.gaia.core.presentation.ArticleContent
import com.yuyakaido.gaia.core.presentation.ArticleItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AccountScreen(
    viewModel: AccountViewModel
) {
    when (val s = viewModel.state.collectAsState().value) {
        is AccountViewModel.State.Loading -> {
            LoadingIndicator()
        }
        is AccountViewModel.State.Ideal -> {
            val account = s.account
            Column {
                AccountInfo(account = account)
                AccountTabHeader(
                    selectedTab = s.selectedTab,
                    onSelectTab = { viewModel.onSelectTab(it) }
                )
                AccountTabContents(
                    selectedTab = s.selectedTab,
                    account = account,
                    posts = s.posts,
                    comments = s.comments,
                    trophies = s.trophies
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AccountInfo(
    account: Account
) {
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
}

@Composable
fun AccountTabHeader(
    selectedTab: AccountTab,
    onSelectTab: (tab: AccountTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = Modifier.height(48.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        AccountTab.values().forEach {
            Tab(
                selected = it == selectedTab,
                onClick = { onSelectTab.invoke(it) },
            ) {
                Text(text = stringResource(id = it.title))
            }
        }
    }
}

@Composable
fun AccountTabContents(
    selectedTab: AccountTab,
    account: Account,
    posts: List<Article>,
    comments: List<Comment>,
    trophies: List<Trophy>
) {
    when (selectedTab) {
        AccountTab.Post -> {
            PostTabContent(posts = posts)
        }
        AccountTab.Comment -> {
            CommentTabContent(comments = comments)
        }
        AccountTab.About -> {
            AboutTabContent(
                account = account,
                trophies = trophies
            )
        }
    }
}

@Composable
fun PostTabContent(
    posts: List<Article>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts) {
            ArticleItem(
                content = ArticleContent(
                    article = it,
                    isProcessing = false
                ),
                showDetail = false,
                onClickArticle = {},
                onClickAuthor = {},
                onToggleVote = {}
            )
        }
    }
}

@Composable
fun CommentTabContent(
    comments: List<Comment>
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(comments) {
            Column {
                Text(text = it.post.title)
                Text(
                    text = "r/${it.community.name}",
                    style = MaterialTheme.typography.caption
                )
                Text(text = it.body)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Divider()
        }
    }
}

@Composable
fun AboutTabContent(
    account: Account,
    trophies: List<Trophy>
) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = account.postKarma.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.account_tab_about_post_karma),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = account.commentKarma.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.account_tab_about_comment_karma),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = account.awarderKarma.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.account_tab_about_awarder_karma),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = account.awardeeKarma.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.account_tab_about_awardee_karma),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
        items(trophies) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.icon,
                    contentDescription = it.name,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.size(12.dp))
        }
    }
}