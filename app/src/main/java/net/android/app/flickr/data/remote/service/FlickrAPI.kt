package net.android.app.flickr.data.remote.service

import net.android.app.flickr.data.remote.modal.ApiResponse
import net.android.app.flickr.data.remote.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrAPI {

    @GET("?method=flickr.interestingness.getList")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ) : ApiResponse

    @GET("?method=flickr.photos.search")
    suspend fun searchPhotos(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ApiResponse

}