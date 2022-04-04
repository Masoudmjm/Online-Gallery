package com.masoudjafari.kiliaro.images

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.masoudjafari.kiliaro.Event
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _thumbnailQueryParameters = MutableLiveData<String>()
    val thumbnailQueryParameters : MutableLiveData<String> = _thumbnailQueryParameters

    private val _openImageEvent = MutableLiveData<Event<String>>()
    val openImageEvent: LiveData<Event<String>> = _openImageEvent

    private val _transitionImage = MutableLiveData<ImageView>()
    val transitionImage: MutableLiveData<ImageView> = _transitionImage

    private val _items: LiveData<List<Image>> = _forceUpdate.switchMap { forceUpdate ->
        var imagesSize = 0
        viewModelScope.launch {
            val result = imagesRepository.getImages(false)
            if (result is Result.Success)
            imagesSize = result.data.size

            if (forceUpdate || imagesSize == 0) {
                _dataLoading.value = true
                val imageResult = imagesRepository.getImages(true)
                if (imageResult is Result.Error)
                    showSnackbarMessage(R.string.loading_images_error)
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
            showSnackbarMessage(R.string.loading_images_error)
            isDataLoadingError.value = true
        }

        return result
    }

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun openImage(imageId: String, imageView: ImageView) {
        _transitionImage.value = imageView
        _openImageEvent.value = Event(imageId)
    }

    fun refresh() {
        _forceUpdate.value = true
    }

    fun setScreenWidth(screenWidth: Int) {
        _thumbnailQueryParameters.value = "?w=${screenWidth/3}&h=${screenWidth/3}&m=crop"
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    companion object {
        private val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String) {
            if (url.isNotEmpty()) {
                Glide.with(view.context)
                    .load(url)
//                    .placeholder(shimmerDrawable)
                    .into(view)
            }
        }
    }
}