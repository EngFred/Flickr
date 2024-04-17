package net.android.app.flickr.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.android.app.flickr.domain.modal.FlickrImage

interface FlickrRepository {
    fun getImages(): Flow<PagingData<FlickrImage>>
    fun searchPhotos( searchQuery: String ): Flow<PagingData<FlickrImage>>
}