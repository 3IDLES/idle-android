package com.idle.network.di

import com.idle.network.BuildConfig
import com.idle.network.model.auth.AuthRequest
import com.idle.network.model.auth.ConfirmRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
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
    fun providesNetworkApi(
        okHttpClient: OkHttpClient,
    ): CareNetworkApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.CARE_BASE_URL).build()
            .create(CareNetworkApi::class.java)
}

interface CareNetworkApi {
    @POST("/api/v1/auth/core/send")
    suspend fun sendAuthNumber(@Body authRequest: AuthRequest): Response<Unit>

    @POST("/api/v1/auth/core/confirm")
    suspend fun confirmAuthNumber(@Body confirmRequest: ConfirmRequest): Response<Unit>
}