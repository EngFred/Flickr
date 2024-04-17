package net.android.app.flickr.domain.modal

data class FlickrImage(
    val id: String,
    val smallSizeImageUrl: String?,
    val mediumSizeImageUrl: String?,
    val largerSizeImageUrl: String?
)
