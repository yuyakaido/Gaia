package com.yuyakaido.gaia.article.presentation.detail

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.gaia.article.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private val viewModel by viewModels<ArticleDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // https://developer.android.com/jetpack/compose/interop/interop-apis
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { ArticleDetailScreen(viewModel = viewModel) }
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
                    menuInflater.inflate(R.menu.menu_article_detail, menu)
                    val voteMenu = menu.findItem(R.id.menu_vote)
                    viewModel.article
                        .onEach {
                            voteMenu.setIcon(
                                when (it.likes) {
                                    true -> R.drawable.ic_menu_voted
                                    false -> R.drawable.ic_menu_unvoted
                                    null -> R.drawable.ic_menu_unvoted
                                }
                            )
                        }
                        .launchIn(viewLifecycleOwner.lifecycleScope)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    viewModel.onToggleVote()
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

}