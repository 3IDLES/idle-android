package com.idle.data.di

import com.idle.data.repository.AuthRepositoryImpl
import com.idle.data.repository.ConfigRepositoryImpl
import com.idle.data.repository.ErrorRepositoryImpl
import com.idle.data.repository.JobPostingRepositoryImpl
import com.idle.data.repository.NotificationRepositoryImpl
import com.idle.data.repository.ProfileRepositoryImpl
import com.idle.data.repository.TokenManagerImpl
import com.idle.data.repository.TokenRepositoryImpl
import com.idle.data.repository.ChatRepositoryImpl
import com.idle.domain.repositorry.AuthRepository
import com.idle.domain.repositorry.ConfigRepository
import com.idle.domain.repositorry.ErrorRepository
import com.idle.domain.repositorry.JobPostingRepository
import com.idle.domain.repositorry.NotificationRepository
import com.idle.domain.repositorry.ProfileRepository
import com.idle.domain.repositorry.TokenRepository
import com.idle.domain.repositorry.ChatRepository
import com.idle.network.di.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl,
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl,
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindsJobPostingRepository(
        jobPostingRepositoryImpl: JobPostingRepositoryImpl,
    ): JobPostingRepository

    @Binds
    @Singleton
    abstract fun bindsConfigRepository(
        configRepositoryImpl: ConfigRepositoryImpl
    ): ConfigRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindsChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl,
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindsErrorRepository(
        errorRepositoryImpl: ErrorRepositoryImpl,
    ): ErrorRepository

    @Binds
    @Singleton
    abstract fun bindsTokenProvider(
        tokenProviderImpl: TokenManagerImpl,
    ): TokenManager
}
