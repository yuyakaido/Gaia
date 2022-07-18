package com.yuyakaido.gaia.article.presentation.list

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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private val viewModel by viewModels<ArticleListViewModel>()

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
                ArticleListScreen(
                    articles = state.articles,
                    isRefreshing = state.isRefreshing,
                    isError = state.isError,
                    onRefresh = { viewModel.onRefresh() },
                    onPaginate = { viewModel.onPaginate() },
                    onClick = {
                        findNavController().navigate(
                            directions = ArticleListFragmentDirections.actionArticleDetail(
                                articleId = it.id.value,
                                articleTitle = it.title
                            )
                        )
                    }
                )
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
                    menuInflater.inflate(R.menu.menu_article, menu)
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