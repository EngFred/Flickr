package net.android.app.flickr.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import net.android.app.flickr.data.remote.mappers.toFlickrImage
import net.android.app.flickr.data.remote.pagingSource.ImagesPagingSource
import net.android.app.flickr.data.remote.pagingSource.SearchedImagesPagingSource
import net.android.app.flickr.data.remote.service.FlickrAPI
import net.android.app.flickr.data.remote.utils.ApiConstants
import net.android.app.flickr.domain.modal.FlickrImage
import net.android.app.flickr.domain.repository.FlickrRepository
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor(
    private val api: FlickrAPI
) : FlickrRepository {

    override fun getImages(): Flow<PagingData<FlickrImage>> = Pager(
        config = PagingConfig(ApiConstants.PER_PAGE_ITEMS),
        pagingSourceFactory = {
            ImagesPagingSource(api = api)
        }
    ).flow.map {
        it.map {
            it.toFlickrImage()
        }
    }.flowOn(Dispatchers.IO)

    override fun searchPhotos(searchQuery: String): Flow<PagingData<FlickrImage>> = Pager(
        config = PagingConfig(ApiConstants.PER_PAGE_ITEMS),
        pagingSourceFactory = {
            SearchedImagesPagingSource(api, searchQuery)
        }
    ).flow.map {
        it.map {
            it.toFlickrImage()
        }
    }.flowOn(Dispatchers.IO)
}