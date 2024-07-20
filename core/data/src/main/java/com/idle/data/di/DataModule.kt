package com.idle.data.di

import com.idle.data.repository.auth.AuthRepositoryImpl
import com.idle.data.repository.auth.TokenManagerImpl
import com.idle.data.repository.auth.TokenRepositoryImpl
import com.idle.data.repository.profile.ProfileRepositoryImpl
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.domain.repositorry.auth.TokenRepository
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.network.token.TokenManager
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
    abstract fun bindsCenterProfileRepository(
        centerProfileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindsTokenProvider(
        tokenProviderImpl: TokenManagerImpl,
    ): TokenManager
}