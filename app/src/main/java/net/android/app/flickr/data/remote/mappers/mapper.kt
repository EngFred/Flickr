package net.android.app.flickr.data.remote.mappers

import net.android.app.flickr.data.remote.modal.FlickrImageDTO
import net.android.app.flickr.domain.modal.FlickrImage

fun FlickrImageDTO.toFlickrImage() : FlickrImage {
    return FlickrImage(
        id = id,
        smallSizeImageUrl = url_s,
        mediumSizeImageUrl = url_m,
        largerSizeImageUrl = url_l
    )
}