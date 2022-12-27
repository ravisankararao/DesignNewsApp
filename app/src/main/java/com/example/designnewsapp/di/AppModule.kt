package com.example.designnewsapp.di

import com.example.designnewsapp.api.NewsApiInterface
import com.example.designnewsapp.data.model.HeadlinesCategory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApiInterface.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApiInterface =
        retrofit.create(NewsApiInterface::class.java)

    @Provides
    @Singleton
    fun provideHeadlinesPreferences(): HeadlinesCategory {
        return HeadlinesCategory(
            "business",
        "entertainment",
        "sports",
        "technology"
        )
    }
}
