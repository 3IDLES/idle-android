package com.idle.network.api

import com.idle.network.model.map.DataVersionResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApi {
    @GET("/map-static/v2/raster")
    suspend fun getStaticMap(
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("markers") markers: List<String>,
        @Query("dataversion") dataVersion: String
    ): Response<ResponseBody>

    @GET("/map-static/v2/lastversion")
    suspend fun getMapDataVersion(): Response<DataVersionResponse>
}