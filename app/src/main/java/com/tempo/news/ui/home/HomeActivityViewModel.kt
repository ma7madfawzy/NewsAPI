package com.tempo.news.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tempo.news.data.model.ArticleDM
import com.tempo.news.data.model.ResponseDM
import com.tempo.news.data.model.Result
import com.tempo.news.data.repositories.NewsRepository
import com.tempo.news.ui.base.BaseLoaderAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivityViewModel(private val repository: NewsRepository) : ViewModel(),
    BaseLoaderAdapter.PaginationHandler {
    val model = HomeActivityModel()

    val newsResult = MutableLiveData<List<ArticleDM>>()

    /**
     * Search a repository based on a query string.
     */
    init {
        loadData(1)
    }

    fun searchRepo(queryString: String, page: Int = 1) {
        // can be launched in a separate asynchronous job
        model.loading = true
        viewModelScope.launch(Dispatchers.IO) {
            onDataLoaded(repository.fetchArticles(queryString, page))
        }
    }

    private fun onDataLoaded(result: Result<ResponseDM<List<ArticleDM>>>) {
        when (result) {
            is Result.Success<ResponseDM<List<ArticleDM>>> -> onSuccess(result)
            is Result.Error -> model.messageText = result.exception
        }
        model.loading = false
    }

    private fun onSuccess(result: Result.Success<ResponseDM<List<ArticleDM>>>) {
        newsResult.postValue(result.data.articles!!)
    }

    override fun onLoadMore(page: Int, totalRows: Int) {
        loadData(page)
    }

    private fun loadData(page: Int) {
        searchRepo(model.queryText.toString(), page)
    }

    fun onQueryChanged(query: String) {
        model.queryText = query
        loadData(1)
    }
}