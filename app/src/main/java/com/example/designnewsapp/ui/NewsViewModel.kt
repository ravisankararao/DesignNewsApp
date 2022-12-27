package com.example.designnewsapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.designnewsapp.data.repository.NewsRepository
import com.example.designnewsapp.data.model.SortParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private const val TAG = "NewsViewModel"

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private var currentQuery: MutableStateFlow<String> = MutableStateFlow("latest")
    private val sortParamsFlow = MutableStateFlow(SortParams().publishedAt)

    private val countryFlow: MutableStateFlow<String?> = MutableStateFlow("in")

    private val categoryFlow: MutableStateFlow<String?> = MutableStateFlow("business")

    private val languageFlow: MutableStateFlow<String?> = MutableStateFlow("en")

    /***************************
    Combine Search query with sort params
     *****************************
     */

    val searchNewsFlow = combine(
        currentQuery,
        sortParamsFlow,
    ) { (query, sort) ->
        QueryWithSort(query, sort)
    }.flatMapLatest {
        repository.getSearchResultStream(it.query, it.sort, null, null, null)
            .cachedIn(viewModelScope)
    }

    /*
      Combine flows of various params for Top-Headlines
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val topHeadlinesFlow =
        combine(
            countryFlow,
            languageFlow,
            categoryFlow
        ) { (countryFlow, languageFlow, categoryFlow) ->
            PreferenceFlows(
                countryFlow,
                languageFlow,
                categoryFlow
            )
        }.flatMapLatest {
            repository.getSearchResultStream(null, null, it.country, it.language, it.category)
        }.cachedIn(viewModelScope)

    //set query in SavedStateHandle
    fun searchNews(query: String) {
        state["query"] = query
        currentQuery.value = state.getLiveData("query", "latest").value.toString()
    }

    fun newsSortedByUser(sort: String) {
        sortParamsFlow.value = sort
    }

    fun topHeadlinesPreferences(language: String?, country: String?, category: String?) {
        languageFlow.value = language
        countryFlow.value = country
        categoryFlow.value = category
    }

    override fun onCleared() {
        state["query"] = null
        super.onCleared()
    }

}

data class PreferenceFlows(
    val country: String?,
    val language: String?,
    val category: String?
)

data class QueryWithSort(
    val query: String,
    val sort: String
)

