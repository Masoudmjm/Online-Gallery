package com.masoudjafari.kiliaro.imageDetail

import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.masoudjafari.kiliaro.Event
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.data.source.ImagesRepository
import javax.inject.Inject
import com.masoudjafari.kiliaro.data.Result
import com.masoudjafari.kiliaro.data.Image
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _imageId = MutableLiveData<String>()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _image = _imageId.switchMap { imageId ->
        imagesRepository.observeImage(imageId).map { computeResult(it) }
    }
    val image: LiveData<Image?> = _image

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
            showSnackbarMessage(R.string.loading_image_error)
            null
        }
    }

    private fun loadImage(view: ImageView, url: String) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}