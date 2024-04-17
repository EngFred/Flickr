package net.android.app.flickr.presentation.fragments.search

import net.android.app.flickr.domain.modal.SearchQuery

sealed class SearchScreenEvents {
    data class SearchQueryChanged( val query: String ) : SearchScreenEvents()
    data class SearchQueryDeletedFromCache( val searchQuery: SearchQuery ) : SearchScreenEvents()
    data object SearchClicked: SearchScreenEvents()
    data class RVQueryClicked( val queryText: String ): SearchScreenEvents()
    data object SearchQueryCleared: SearchScreenEvents()
}