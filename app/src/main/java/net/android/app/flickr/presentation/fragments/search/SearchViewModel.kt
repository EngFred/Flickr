package net.android.app.flickr.presentation.fragments.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import net.android.app.flickr.domain.modal.FlickrImage
import net.android.app.flickr.domain.modal.SearchQuery
import net.android.app.flickr.domain.useCases.local.AddQueryUseCase
import net.android.app.flickr.domain.useCases.local.DeleteQueryUseCase
import net.android.app.flickr.domain.useCases.local.GetQueryByTextUseCase
import net.android.app.flickr.domain.useCases.local.GetUserQueriesUseCase
import net.android.app.flickr.domain.useCases.local.SearchQueryUseCase
import net.android.app.flickr.domain.useCases.local.SetLastSearchQueryUseCase
import net.android.app.flickr.domain.useCases.remote.SearchPhotosUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase,
    private val searchQueryUseCase: SearchQueryUseCase,
    private val getUserQueriesUseCase: GetUserQueriesUseCase,
    private val addQueryUseCase: AddQueryUseCase,
    private val deleteQueryUseCase: DeleteQueryUseCase,
    private val getQueryByTextUseCase: GetQueryByTextUseCase,
    private val setLastSearchQueryUseCase: SetLastSearchQueryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PagingData<FlickrImage>>(PagingData.empty())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchQueries = MutableStateFlow<List<SearchQuery>>(emptyList())
    val searchQueries = _searchQueries.asStateFlow()

    init {
        getUserQueries()
    }

    fun onEvent( event: SearchScreenEvents ) {
        when(event) {
            SearchScreenEvents.SearchClicked -> {
                if ( _searchQuery.value.isNotEmpty() ) {
                    setLastSearchQuery( _searchQuery.value.trim() )
                    addQuery( SearchQuery(queryText = _searchQuery.value.trim()) )
                    searchPhotos( _searchQuery.value.trim() )
                    _searchQuery.value = ""
                }
            }
            is SearchScreenEvents.SearchQueryChanged -> {
                _searchQuery.value = event.query
                getRelatedQueries(event.query)
            }

            SearchScreenEvents.SearchQueryCleared -> {
                if ( _searchQuery.value.isNotEmpty() ) {
                    _searchQuery.value = ""
                }
            }

            is SearchScreenEvents.SearchQueryDeletedFromCache -> {
                deleteQuery( event.searchQuery )
            }

            is SearchScreenEvents.RVQueryClicked -> {
                searchPhotos( event.queryText )
            }

        }
    }

    private fun searchPhotos(query: String ) = viewModelScope.launch {
        searchPhotosUseCase.invoke( query )
            .cachedIn(viewModelScope)
            .collectLatest {
            _uiState.value = it
        }
    }

    private fun setLastSearchQuery( queryText: String ) = viewModelScope.launch(Dispatchers.IO) {
        setLastSearchQueryUseCase.invoke( queryText )
    }

    private fun addQuery( searchQuery: SearchQuery ) = viewModelScope.launch( Dispatchers.IO ) {
        getQueryByTextUseCase.invoke( searchQuery.queryText ).collectLatest {
            if ( it == null ) {
                addQueryUseCase.invoke( searchQuery )
                Log.d("KOTLIN", "New query is added!!!")
            } else {
                Log.i("KOTLIN", "Query with that text already exists, so not added!")
            }
        }
    }

    private fun deleteQuery( searchQuery: SearchQuery ) = viewModelScope.launch( Dispatchers.IO ) {
        deleteQueryUseCase.invoke( searchQuery )
    }

    private fun getUserQueries() = viewModelScope.launch {
        getUserQueriesUseCase.invoke().collectLatest {
            _searchQueries.value = it
        }
    }

    @OptIn(FlowPreview::class)
    private fun getRelatedQueries(queryText: String ) = viewModelScope.launch {
        searchQueryUseCase.invoke(queryText)
            .debounce(200)
            .collectLatest {
            if ( it.isNotEmpty() ) _searchQueries.value = it
        }
    }
}