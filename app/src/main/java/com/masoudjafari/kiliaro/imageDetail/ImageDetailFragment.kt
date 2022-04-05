package com.masoudjafari.kiliaro.imageDetail

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.databinding.FragmentImageDetailBinding
import com.masoudjafari.kiliaro.util.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailFragment : Fragment() {
    private lateinit var binding: FragmentImageDetailBinding
    private val args: ImageDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<ImageDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_image_detail, container, false)
        binding = FragmentImageDetailBinding.bind(view).apply {
            viewmodel = viewModel
        }
        // to run transition
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.start(args.imageId)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (width, height) = getScreenWidth(requireContext())
        viewModel.setScreenWidth(width, height)
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun getScreenWidth(context: Context): Pair<Int,Int> {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        return Pair(metrics.widthPixels, metrics.heightPixels)
    }
}