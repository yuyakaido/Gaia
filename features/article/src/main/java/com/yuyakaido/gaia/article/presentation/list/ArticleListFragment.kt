package com.yuyakaido.gaia.article.presentation.list

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.yuyakaido.gaia.article.R
import com.yuyakaido.gaia.article.domain.ArticleSort
import com.yuyakaido.gaia.core.misc.Sharing
import com.yuyakaido.gaia.core.presentation.AppNavigatorType
import com.yuyakaido.gaia.core.presentation.GaiaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private val viewModel by viewModels<ArticleListViewModel>()

    @Inject
    internal lateinit var  appNavigator: AppNavigatorType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // https://developer.android.com/jetpack/compose/interop/interop-apis
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state = viewModel.state.collectAsState().value
                GaiaTheme {
                    ArticleListScreen(
                        contents = state.contents,
                        isRefreshing = state.isRefreshing,
                        isError = state.isError,
                        onRefresh = { viewModel.onRefresh() },
                        onPaginate = { viewModel.onPaginate() },
                        onClickArticle = {
                            findNavController().navigate(
                                directions = ArticleListFragmentDirections.actionArticleDetail(
                                    articleId = it.id.value,
                                    articleTitle = it.title
                                )
                            )
                        },
                        onClickAuthor = {
                            appNavigator.navigateToAccount(
                                navController = findNavController(),
                                name = it.name
                            )
                        },
                        onToggleVote = { viewModel.onToggleVote(it) },
                        onClickShare = {
                            Sharing.share(
                                article = it,
                                activity = requireActivity()
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOptionMenu()
    }

    private fun setupOptionMenu() {
        val menuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_article_list, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    viewModel.onSwitchSort(ArticleSort.from(menuItem.itemId))
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

}