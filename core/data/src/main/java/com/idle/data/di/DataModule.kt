package com.idle.data.di

import com.idle.data.TokenProviderImpl
import com.idle.data.repository.AuthRepositoryImpl
import com.idle.domain.repositorry.auth.AuthRepository
import com.idle.network.token.TokenProvider
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
    abstract fun bindsTokenProvider(
        tokenProviderImpl: TokenProviderImpl,
    ): TokenProvider
}