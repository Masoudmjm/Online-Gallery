package com.masoudjafari.kiliaro.imageDetail

import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.masoudjafari.kiliaro.Event
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import javax.inject.Inject
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.Image
import com.masoudjafari.kiliaro.util.ResizeMode
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _imageId = MutableLiveData<String>()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _thumbnailQueryParameters = MutableLiveData<String>()
    val thumbnailQueryParameters : MutableLiveData<String> = _thumbnailQueryParameters

    private val _image = _imageId.switchMap { imageId ->
        imagesRepository.observeImage(imageId).map { computeResult(it) }
    }
    val image: LiveData<Image?> = _image

    val isDataAvailable: LiveData<Boolean> = _image.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun start(imageId: String) {
        if (_dataLoading.value == true || imageId == _imageId.value)
            return
        _imageId.value = imageId
    }

    private fun computeResult(imageResult: Result<Image>): Image? {
        return if (imageResult is Result.Success) {
            imageResult.data
        } else {
            showSnackbarMessage(R.string.loading_images_error)
            null
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

    fun setScreenWidth(screenWidth: Int, screenHeight: Int) {
        _thumbnailQueryParameters.value = "?w=${screenWidth}&h=${screenHeight}&m=${ResizeMode.MINIMUM_DIMENSION.value}"
    }

    companion object {
        @JvmStatic
        private val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(1800)
            .setBaseAlpha(0.98f)
            .setHighlightAlpha(0.92f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        @JvmStatic
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String) {
            if (url.isNotEmpty()) {
                Glide.with(view.context)
                    .load(url)
                    .apply(RequestOptions()
                        .override(Target.SIZE_ORIGINAL))
                    .placeholder(shimmerDrawable)
                    .error(R.drawable.error_load_image)
                    .into(view)
            }
        }
    }
}