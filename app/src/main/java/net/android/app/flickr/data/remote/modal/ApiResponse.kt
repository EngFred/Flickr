package net.android.app.flickr.data.remote.modal

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val photos: PhotosResponse
)

data class PhotosResponse(
    val page: Int,
    @SerializedName("pages")
    val totalPages: Int,
    @SerializedName("perpage")
    val perPage: Int,
    val total: Int,
    @SerializedName("photo")
    val photosList: List<FlickrImageDTO>
)
