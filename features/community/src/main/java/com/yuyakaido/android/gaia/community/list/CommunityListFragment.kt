package com.yuyakaido.android.gaia.community.list

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import com.yuyakaido.android.gaia.community.databinding.FragmentCommunityListBinding
import com.yuyakaido.android.gaia.core.domain.entity.Community
import com.yuyakaido.android.gaia.core.domain.extension.dpToPx
import com.yuyakaido.android.gaia.core.presentation.AppNavigatorType
import com.yuyakaido.android.gaia.core.presentation.BaseFragmentWithoutHilt
import javax.inject.Inject

class CommunityListFragment : BaseFragmentWithoutHilt<CommunityListViewModel>() {

  companion object {
    fun newInstance(): Fragment {
      return CommunityListFragment()
    }
  }

  @Inject
  internal lateinit var appNavigator: AppNavigatorType

  override val viewModel: CommunityListViewModel by viewModels { factory }
  private lateinit var binding: FragmentCommunityListBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCommunityListBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupRecyclerView()
    setupSwipeRefreshLayout()
    setupRetryButton()
  }

  private fun setupRecyclerView() {
    val manager = LinearLayoutManager(requireContext())
    val adapter = GroupAdapter<GroupieViewHolder>()
    binding.recyclerView.layoutManager = manager
    binding.recyclerView.adapter = adapter
    binding.recyclerView.addItemDecoration(
      HorizontalDividerItemDecoration.Builder(requireContext())
        .color(Color.TRANSPARENT)
        .size(8.dpToPx(requireContext()))
        .build()
    )
    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) = Unit
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val itemCount = manager.itemCount
        val lastVisibleItemPosition = manager.findLastVisibleItemPosition()
        if (lastVisibleItemPosition >= itemCount - 1) {
          viewModel.onPaginate()
        }
      }
    })

    val listener = { community: Community.Detail ->
      val summary = Community.Summary(name = community.name)
      appNavigator.navigateToCommunityDetail(
        controller = findNavController(),
        community = summary
      )
    }

    viewModel.state
      .observe(viewLifecycleOwner) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.progressVisibility
        binding.retryButton.visibility = state.retryVisibility
        // Workaround: ????????????????????????ItemDecoration????????????????????????????????????
        if (state.communities.isEmpty()) {
          adapter.clear()
        }
        // Workaround:
        // GroupAdapter.updateAsync???????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????GroupAdapter.update??????????????????????????????????????????????????????????????????????????????
        adapter.update(
          state.communities.map { community ->
            CommunityItem(
              community = community,
              listener = listener
            )
          }
        )
      }
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayout.setOnRefreshListener {
      viewModel.onRefresh()
    }
  }

  private fun setupRetryButton() {
    binding.retryButton.setOnClickListener {
      viewModel.onRetry()
    }
  }

}