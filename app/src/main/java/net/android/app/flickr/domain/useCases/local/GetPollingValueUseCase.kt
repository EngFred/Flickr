package net.android.app.flickr.domain.useCases.local

import net.android.app.flickr.data.local.preferences.PreferencesRepository
import javax.inject.Inject

class GetPollingValueUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke() = preferencesRepository.isPolling
}