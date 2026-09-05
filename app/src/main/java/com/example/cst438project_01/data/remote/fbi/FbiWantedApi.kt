package com.example.cst438project_01.data.remote.fbi

import retrofit2.http.GET
import retrofit2.http.Query

interface FbiWantedApi {

    @GET("wanted/v1/list")
    suspend fun getWantedPeople(
        @Query("page") page: Int = 1,
        @Query("field_offices") fieldOffice: String? = null,
        @Query("title") title: String? = null
    ): FbiWantedResponse
}
