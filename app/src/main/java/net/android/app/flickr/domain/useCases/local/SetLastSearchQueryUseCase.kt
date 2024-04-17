package net.android.app.flickr.domain.useCases.local

import net.android.app.flickr.data.local.preferences.PreferencesRepository
import javax.inject.Inject

class SetLastSearchQueryUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
)  {
    suspend operator fun invoke( queryText: String ) = preferencesRepository.setStoredQuery(queryText)
}