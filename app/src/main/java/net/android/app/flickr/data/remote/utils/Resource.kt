package net.android.app.flickr.data.remote.utils

sealed class Resource<out T> {
    data class Success<T>( val data: T ) : Resource<T>()
    data class Error( val errorMessage: String ) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}