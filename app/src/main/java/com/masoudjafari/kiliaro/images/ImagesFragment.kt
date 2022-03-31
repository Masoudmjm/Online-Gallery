package com.masoudjafari.kiliaro.images

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.masoudjafari.kiliaro.EventObserver
import com.masoudjafari.kiliaro.ImagesAdapter
import com.masoudjafari.kiliaro.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private val viewModel by viewModels<ImagesViewModel>()
    private lateinit var binding: FragmentImagesBinding
    private lateinit var listAdapter: ImagesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentImagesBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setLayoutManager()
        setupListAdapter()
        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.openImageEvent.observe(viewLifecycleOwner, EventObserver {
            openTaskDetails(it)
        })
    }

    private fun setLayoutManager() {
        binding.imagesList.layoutManager = GridLayoutManager(context, 3,GridLayoutManager.VERTICAL, false)
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

    private fun openTaskDetails(imageId: String) {
        val action = ImagesFragmentDirections.actionImagesFragmentToDetailFragment(/*imageId*/)
        findNavController().navigate(action)
    }
}