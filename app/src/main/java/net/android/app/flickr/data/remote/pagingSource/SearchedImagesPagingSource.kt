package net.android.app.flickr.data.remote.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.android.app.flickr.data.remote.modal.FlickrImageDTO
import net.android.app.flickr.data.remote.service.FlickrAPI
import net.android.app.flickr.data.remote.utils.ApiConstants
import java.net.ConnectException
import javax.inject.Inject

class SearchedImagesPagingSource @Inject constructor(
    private val api: FlickrAPI,
    private val searchQuery: String
) : PagingSource<Int, FlickrImageDTO>() {

    companion object {
        private val TAG = SearchedImagesPagingSource::class.java.simpleName
    }

    override fun getRefreshKey(state: PagingState<Int, FlickrImageDTO>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlickrImageDTO> {
        return try {
            val currentPage = params.key ?: 1
            val response = api.searchPhotos(searchQuery, currentPage, ApiConstants.PER_PAGE_ITEMS).photos
            val endOfPagination = response.page == response.totalPages

            if ( response.photosList.isNotEmpty() ) {
                LoadResult.Page(
                    data = response.photosList,
                    prevKey = if (currentPage == 1) null else (currentPage - 1),
                    nextKey = if (endOfPagination) null else (currentPage + 1)
                )
            } else {
                Log.d("KOTLIN", "Nothing was found!!")
                LoadResult.Error(Throwable("Nothing was found!!"))
            }

        }catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

}