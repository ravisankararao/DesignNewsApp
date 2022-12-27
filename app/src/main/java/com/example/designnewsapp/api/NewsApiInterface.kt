package com.example.designnewsapp.api

import com.example.designnewsapp.BuildConfig
import com.example.designnewsapp.data.model.NewsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface NewsApiInterface {

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val KEY = BuildConfig.ACCESS_KEY
    }

    @Headers("X-Api-Key: $KEY ")
    @GET("v2/everything")
    suspend fun searchNewsArticles(
        @Query("q") query: String,
        @Query("sortBy") sortParams: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): NewsSearchResponse

    @Headers("X-Api-Key: $KEY ")
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String?,
        @Query("language") language: String?,
        @Query("category") category: String?
    ): NewsSearchResponse
}
