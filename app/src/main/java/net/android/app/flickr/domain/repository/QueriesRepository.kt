package net.android.app.flickr.domain.repository

import kotlinx.coroutines.flow.Flow
import net.android.app.flickr.domain.modal.SearchQuery

interface QueriesRepository {
    fun getUserQueries() : Flow<List<SearchQuery>>
    suspend fun deleteQuery( searchQuery: SearchQuery )
    suspend fun addQuery( searchQuery: SearchQuery )
    fun searchQuery( queryText: String ) : Flow<List<SearchQuery>>
    fun getQueryByText(queryText: String): Flow<SearchQuery?>
}