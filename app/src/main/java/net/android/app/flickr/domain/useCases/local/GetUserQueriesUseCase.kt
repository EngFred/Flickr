package net.android.app.flickr.domain.useCases.local

import net.android.app.flickr.domain.repository.QueriesRepository
import javax.inject.Inject

class GetUserQueriesUseCase @Inject constructor(
    private val queriesRepository: QueriesRepository
)  {
    operator fun invoke() = queriesRepository.getUserQueries()
}