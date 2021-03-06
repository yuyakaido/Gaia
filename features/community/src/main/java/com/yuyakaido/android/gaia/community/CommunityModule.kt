package com.yuyakaido.android.gaia.community

import com.yuyakaido.android.gaia.core.SignedInComponent
import com.yuyakaido.android.gaia.core.domain.app.SignedInScope
import com.yuyakaido.android.gaia.core.domain.repository.CommunityRepositoryType
import com.yuyakaido.android.gaia.core.infrastructure.RetrofitForPrivate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import retrofit2.Retrofit

@InstallIn(SignedInComponent::class)
@Module
class CommunityModule {

  @SignedInScope
  @Provides
  fun provideCommunityApi(
    @RetrofitForPrivate retrofit: Retrofit
  ): CommunityApi {
    return retrofit.create(CommunityApi::class.java)
  }

  @SignedInScope
  @Provides
  fun provideCommunityRepositoryType(
    api: CommunityApi
  ): CommunityRepositoryType {
    return CommunityRepository(
      api = api
    )
  }

}