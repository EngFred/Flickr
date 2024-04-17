package net.android.app.flickr.presentation.fragments.photos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.android.app.flickr.R
import net.android.app.flickr.data.worker.PollWorker
import net.android.app.flickr.databinding.FragmentPhotosBinding
import net.android.app.flickr.presentation.adapters.ImagesAdapter
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PhotosFragment : Fragment(), ImagesAdapter.ImageClickListener {

    private var _binding: FragmentPhotosBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private val photosViewModel : PhotosViewModel by viewModels()
    private val imagesAdapter = ImagesAdapter()

    companion object {
        private const val POLL_WORK = "POLL_WORK"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imagesAdapter.initImageClickListener(this)
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                photosViewModel.uiState.collectLatest {
                    imagesAdapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                photosViewModel.isPolling.collectLatest { isPolling ->
                    val pollingTVText = if (isPolling) getString(R.string.stop_polling) else getString(R.string.start_polling)
                    binding.pollingTv.text = pollingTVText
                    if (isPolling) {
                        Log.i("KOTLIN", "Executing background task!!")
                        val constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()

                        val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
                                .setConstraints(constraints)
                                .build()

                        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                            POLL_WORK,
                            ExistingPeriodicWorkPolicy.KEEP,
                            periodicRequest
                        )

                    } else {
                        Log.d("KOTLIN", "Not executing any background task!!")
                        WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
                    }
                }
            }
        }

        binding.searchView.setOnClickListener {
            findNavController().navigate(PhotosFragmentDirections.photosFragmentToSearchFragment())
        }

        binding.moreIcon.setOnClickListener {
            togglePollingItemVisibility()
        }

        binding.root.setOnClickListener {
            if (binding.pollingTv.visibility == VISIBLE) binding.pollingTv.visibility = GONE
        }

        binding.pollingTv.setOnClickListener {
            togglePollingItemVisibility()
            photosViewModel.togglePollingValue()
        }

        binding.photosRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imagesAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imagesAdapter.loadStateFlow.collectLatest {

                    when (it.refresh) {
                        is LoadState.Loading -> {
                            showLoadingIndicator()
                        }

                        is LoadState.Error -> {
                            showErrorIndicator()
                        }

                        else -> {
                            hideLoadingAndErrorIndicators()
                            when (it.append) {
                                is LoadState.Error -> {
                                    showAppendContainer()
                                    showLoadingMoreErrorIndicator()
                                }

                                is LoadState.Loading -> {
                                    showAppendContainer()
                                    showLoadingMoreProgressIndicator()
                                }

                                else -> {
                                    hideAppendContainer()
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    private fun showLoadingIndicator() {
        binding.loadingIndicator.visibility = VISIBLE
        binding.refreshErrorIndicatorContainer.visibility = GONE
    }

    private fun showAppendContainer() {
        binding.appendIndicatorContainer.visibility = VISIBLE
    }

    private fun hideAppendContainer() {
        binding.appendIndicatorContainer.visibility = GONE
    }

    private fun showLoadingMoreProgressIndicator() {
        binding.appendProgressBar.visibility = VISIBLE
        binding.appendErrorMessageText.visibility = GONE
        binding.appendRetryBtn.visibility = GONE
    }

    private fun showLoadingMoreErrorIndicator() {
        binding.appendErrorMessageText.visibility = VISIBLE
        binding.appendProgressBar.visibility = GONE
        binding.appendRetryBtn.visibility = VISIBLE
        binding.appendErrorMessageText.text = "Unable to load more images"
        binding.appendRetryBtn.setOnClickListener {
            imagesAdapter.retry()
        }
    }

    private fun showErrorIndicator() {
        binding.loadingIndicator.visibility = GONE
        binding.refreshErrorIndicatorContainer.visibility = VISIBLE
        binding.refreshRetryBtn.setOnClickListener {
            imagesAdapter.refresh()
        }
    }

    private fun hideLoadingAndErrorIndicators() {
        binding.loadingIndicator.visibility = GONE
        binding.refreshErrorIndicatorContainer.visibility = GONE
    }

    private fun togglePollingItemVisibility() {
        if (binding.pollingTv.visibility == GONE) binding.pollingTv.visibility = VISIBLE else binding.pollingTv.visibility = GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageClick(imageUrl: String) {
        findNavController().navigate(PhotosFragmentDirections.photosFragmentToImageViewFragment(imageUrl))
    }
}