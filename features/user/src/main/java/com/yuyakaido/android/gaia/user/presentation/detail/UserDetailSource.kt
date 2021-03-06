package com.yuyakaido.android.gaia.user.presentation.detail

import android.os.Parcelable
import com.yuyakaido.android.gaia.core.domain.entity.User
import com.yuyakaido.android.gaia.core.domain.repository.UserRepositoryType
import kotlinx.android.parcel.Parcelize

sealed class UserDetailSource : Parcelable {

  abstract fun page(): UserDetailPage
  abstract suspend fun detail(
    repository: UserRepositoryType
  ): User.Detail

  @Parcelize
  object Me : UserDetailSource() {
    override fun page(): UserDetailPage {
      return UserDetailPage.Me
    }
    override suspend fun detail(
      repository: UserRepositoryType
    ): User.Detail.Me {
      return repository.me()
    }
  }

  @Parcelize
  data class Other(
    val user: User
  ) : UserDetailSource() {
    override fun page(): UserDetailPage {
      return UserDetailPage.Other
    }
    override suspend fun detail(
      repository: UserRepositoryType
    ): User.Detail {
      return repository.detail(user = user)
    }
  }

}