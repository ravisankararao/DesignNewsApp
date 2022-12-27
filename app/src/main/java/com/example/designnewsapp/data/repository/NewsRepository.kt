package com.example.designnewsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.designnewsapp.api.NewsApiInterface
import com.example.designnewsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val service: NewsApiInterface,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResultStream(
        query: String?,
        sortBy: String?,
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    service,
                    query,
                    sortBy,
                    country,
                    language,
                    category
                )
            }
        ).flow

    }

}