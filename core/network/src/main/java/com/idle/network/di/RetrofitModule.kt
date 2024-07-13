package com.idle.network.di

import com.idle.network.BuildConfig
import com.idle.network.api.CareNetworkApi
import com.idle.network.api.TokenNetworkApi
import com.idle.network.token.TokenAuthenticator
import com.idle.network.token.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    @Named("Common")
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .authenticator(tokenAuthenticator)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    @Named("Token")
    fun provideTokenOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun providesCareNetworkApi(
        @Named("Common") okHttpClient: OkHttpClient,
    ): CareNetworkApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.CARE_BASE_URL).build()
            .create(CareNetworkApi::class.java)


    @Singleton
    @Provides
    fun providesTokenNetworkApi(
        @Named("Token") okHttpClient: OkHttpClient,
    ): TokenNetworkApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.CARE_BASE_URL).build()
            .create(TokenNetworkApi::class.java)
}