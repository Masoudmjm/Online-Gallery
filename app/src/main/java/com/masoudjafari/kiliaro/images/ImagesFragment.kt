package com.masoudjafari.kiliaro.images

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.masoudjafari.kiliaro.EventObserver
import com.masoudjafari.kiliaro.R
import com.masoudjafari.kiliaro.databinding.FragmentImagesBinding
import com.masoudjafari.kiliaro.util.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private val viewModel by viewModels<ImagesViewModel>()
    private lateinit var binding: FragmentImagesBinding
    private lateinit var listAdapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListLayoutManager()
        setupListAdapter()
        setupNavigation()
        setHasOptionsMenu(true)
        viewModel.setQueryParametersFromScreenWidth(getScreenWidth(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.images_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.refreshFromRemote()
                true
            }
            else -> false
        }
    }

    private fun setupNavigation() {
        viewModel.openImageEvent.observe(viewLifecycleOwner, EventObserver {
            openImageDetail(it, viewModel.transitionImage.value!!)
        })
    }

    private fun setupListLayoutManager() {
        binding.imagesList.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = ImagesAdapter(viewModel)
            binding.imagesList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun openImageDetail(imageId: String, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to "ImageDetailTransition")
        val action = ImagesFragmentDirections.actionImagesFragmentToDetailFragment(imageId)
        findNavController().navigate(action, extras)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        return metrics.widthPixels
    }

}