package net.android.app.flickr.domain.useCases.remote

import net.android.app.flickr.domain.repository.FlickrRepository
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(
    private val flickrRepository: FlickrRepository
) {
    operator fun invoke(searchQuery: String) = flickrRepository.searchPhotos(searchQuery)
}