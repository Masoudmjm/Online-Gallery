package com.masoudjafari.kiliaro.images

import androidx.lifecycle.*
import com.masoudjafari.kiliaro.Event
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openImageEvent: LiveData<Event<String>> = _openTaskEvent

    private val _items: LiveData<List<Image>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                imagesRepository.getImages(forceUpdate)
                _dataLoading.value = false
            }
        }
        imagesRepository.observeImages().distinctUntilChanged().switchMap { filterImages(it) }
    }

    val items: LiveData<List<Image>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private fun filterImages(imageResult: Result<List<Image>>): LiveData<List<Image>> {
        val result = MutableLiveData<List<Image>>()

        if (imageResult is Result.Success) {
            isDataLoadingError.value = false
            result.value = imageResult.data!!
        } else {
            result.value = emptyList()
            // TODO show error to user
            isDataLoadingError.value = true
        }

        return result
    }

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun openTask(imageId: String) {
        _openTaskEvent.value = Event(imageId)
    }

    fun loadTasks(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun refresh() {
        _forceUpdate.value = true
    }
}