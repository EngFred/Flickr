package net.android.app.flickr.presentation.fragments.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.android.app.flickr.domain.modal.FlickrImage
import net.android.app.flickr.domain.useCases.local.GetPollingValueUseCase
import net.android.app.flickr.domain.useCases.local.SetPollingValueUseCase
import net.android.app.flickr.domain.useCases.remote.GetImagesUseCase
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val setPollingValueUseCase: SetPollingValueUseCase,
    private val getPollingValueUseCase: GetPollingValueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PagingData<FlickrImage>>(PagingData.empty())
    val uiState = _uiState.asStateFlow()

    private val _isPolling = MutableStateFlow(false)
    val isPolling = _isPolling.asStateFlow()

    init {
        getImages()
        getPollingValue()
    }

    private fun getImages() = viewModelScope.launch {
        getImagesUseCase.invoke()
            .cachedIn(viewModelScope)
            .collectLatest {
            _uiState.value = it
        }
    }

    private fun getPollingValue() = viewModelScope.launch {
        getPollingValueUseCase.invoke().collectLatest {
            _isPolling.value = it
        }
    }

    fun togglePollingValue() = viewModelScope.launch( Dispatchers.IO ) {
        setPollingValueUseCase.invoke(isPolling = !_isPolling.value)
    }

}