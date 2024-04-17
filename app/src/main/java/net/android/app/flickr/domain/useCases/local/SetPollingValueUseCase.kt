package net.android.app.flickr.domain.useCases.local

import net.android.app.flickr.data.local.preferences.PreferencesRepository
import javax.inject.Inject

class SetPollingValueUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke( isPolling: Boolean ) = preferencesRepository.setPolling( isPolling )
}