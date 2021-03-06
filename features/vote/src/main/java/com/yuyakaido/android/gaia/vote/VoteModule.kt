package com.yuyakaido.android.gaia.vote

import com.yuyakaido.android.gaia.core.SignedInComponent
import com.yuyakaido.android.gaia.core.domain.app.SignedInScope
import com.yuyakaido.android.gaia.core.domain.repository.VoteRepositoryType
import com.yuyakaido.android.gaia.core.infrastructure.RetrofitForPrivate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import retrofit2.Retrofit

@InstallIn(SignedInComponent::class)
@Module
class VoteModule {

  @SignedInScope
  @Provides
  fun provideVoteApi(
    @RetrofitForPrivate retrofit: Retrofit
  ): VoteApi {
    return retrofit.create(VoteApi::class.java)
  }

  @SignedInScope
  @Provides
  fun provideVoteRepositoryType(
    api: VoteApi
  ): VoteRepositoryType {
    return VoteRepository(
      api = api
    )
  }

}