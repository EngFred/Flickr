package net.android.app.flickr.domain.useCases.local

import net.android.app.flickr.domain.modal.SearchQuery
import net.android.app.flickr.domain.repository.QueriesRepository
import javax.inject.Inject

class AddQueryUseCase @Inject constructor(
    private val queriesRepository: QueriesRepository
)  {
    suspend operator fun invoke( searchQuery: SearchQuery ) = queriesRepository.addQuery( searchQuery )
}