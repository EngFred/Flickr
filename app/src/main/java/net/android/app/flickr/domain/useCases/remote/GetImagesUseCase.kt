package net.android.app.flickr.domain.useCases.remote

import net.android.app.flickr.domain.repository.FlickrRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val flickrRepository: FlickrRepository
) {
    operator fun invoke() = flickrRepository.getImages()
}