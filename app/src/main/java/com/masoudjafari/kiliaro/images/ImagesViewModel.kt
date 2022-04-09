package com.masoudjafari.kiliaro.images

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.masoudjafari.kiliaro.Event
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import com.masoudjafari.kiliaro.util.ResizeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    most values and functions are private,
    This protects the app data inside the ViewModel from unwanted and unsafe changes by external classes,
    but it allows external callers to safely access its value using public values.
*/
@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val gridColumns = 3

    //to show loading icon to user while loading data
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    // set data to _updateFromRemote makes _images to get data based on _updateFromRemote value
    private val _updateFromRemote = MutableLiveData(false)

    // private images list based on _updateFromRemote value
    //when we need to return a different Live Data instance based on the value of another one,
    // we can use Transformations
    // https://developer.android.com/topic/libraries/architecture/livedata#transform_livedata
    private val _images: LiveData<List<Image>> =
        Transformations.switchMap(_updateFromRemote) { updateFromRemote ->
            val result = MutableLiveData<List<Image>>()
            viewModelScope.launch {

                //get Images from database
                val databaseImages = imagesRepository.getImages(false)
                if (databaseImages is Result.Success)
                    result.value = databaseImages.data
                else
                    result.value = emptyList()

                // if database is empty or user requests to refresh, get images from remote
                if (updateFromRemote || result.value!!.isEmpty()) {
                    _dataLoading.value = true
                    val remoteImages = imagesRepository.getImages(true)
                    if (remoteImages is Result.Success)
                        result.value = remoteImages.data
                    else if (remoteImages is Result.Error) {
                        result.value = emptyList()
                        showSnackbarMessage(R.string.loading_images_error)
                    }
                    _dataLoading.value = false
                }
            }
            result
        }

    //public images list
    val images: LiveData<List<Image>> = _images

    // when use select a image, set a transition image to run shared element animation
    private val _transitionImage = MutableLiveData<ImageView>()
    val transitionImage: MutableLiveData<ImageView> = _transitionImage

    // open Image val
    private val _openImageEvent = MutableLiveData<Event<String>>()
    val openImageEvent: LiveData<Event<String>> = _openImageEvent

    // set open Image event and pass the selected image to ImageDetailFragment
    fun openImage(imageId: String, imageView: ImageView) {
        _transitionImage.value = imageView
        _openImageEvent.value = Event(imageId)
    }

    // refresh images (set _updateFromRemote true to get images from remote source)
    fun refreshFromRemote() {
        _updateFromRemote.value = true
    }

    //get resolution from UI and set query parameters to get best resolution image
    private val _thumbnailQueryParameters = MutableLiveData<String>()
    val thumbnailQueryParameters: MutableLiveData<String> = _thumbnailQueryParameters
    fun setQueryParametersFromScreenWidth(screenWidth: Int) {
        _thumbnailQueryParameters.value =
            "?w=${screenWidth / gridColumns}&h=${screenWidth / gridColumns}&m=${ResizeMode.CROPPED.value}"
    }

    // show a message to user
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    companion object {
        // loading image into ImageView to use in DataBinding
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String) {
            if (url.isNotEmpty()) {
                Glide.with(view.context)
                    .load(url)
                    .error(R.drawable.error_load_image)
                    .into(view)
            }
        }
    }
}