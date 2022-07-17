package com.yuyakaido.gaia.article.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
                ArticleListScreen(
                    viewModel = viewModel,
                    onClick = {
                        findNavController().navigate(
                            directions = ArticleListFragmentDirections.actionArticleDetail(
                                articleId = it.id,
                                articleTitle = it.title
                            )
                        )
                    }
                )
            }
        }
    }

}