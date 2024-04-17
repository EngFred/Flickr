package net.android.app.flickr.presentation.fragments.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.android.app.flickr.databinding.FragmentSearchBinding
import net.android.app.flickr.domain.modal.SearchQuery
import net.android.app.flickr.presentation.adapters.QueriesAdapter
import net.android.app.flickr.presentation.adapters.SearchAdapter

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.ImageClickListener, QueriesAdapter.QueryClickedListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = checkNotNull(_binding)

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter()
    private val queriesAdapter = QueriesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter.initImageClickListener(this)
        queriesAdapter.initializeQueryClickListener(this)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                searchViewModel.uiState.collectLatest {
                    searchAdapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchQueries.collectLatest {
                    queriesAdapter.saveData( it )
                }
            }
        }

        setRecyclerView()
        registerClickEvents()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                searchViewModel.searchQuery.collectLatest {
                    /*
                        check whether the existing value and the
                        new value being passed in are different. If they are different, then you
                        update the EditText. If they are the same, you do nothing. This will
                        prevent an infinite loop when you start listening to changes on the
                        EditText.
                     */
                    if ( binding.searchEt.text.toString() != it ) binding.searchEt.setText(it)
                    if ( it.isNotEmpty() ) binding.closeBtn.visibility = View.VISIBLE else binding.closeBtn.visibility = View.INVISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchAdapter.loadStateFlow.collectLatest {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            showLoadingIndicator()
                        }

                        is LoadState.Error -> {
                            showErrorIndicator()
                        }
                        else -> {
                            hideLoadingAndErrorIndicator()
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.photosRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = searchAdapter
        }
        binding.queriesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = queriesAdapter
        }
    }

    private fun registerClickEvents() {
        binding.searchEt.doOnTextChanged { text, _, _, _ ->
            searchViewModel.onEvent(SearchScreenEvents.SearchQueryChanged(text.toString()))
        }

        binding.closeBtn.setOnClickListener {
            searchViewModel.onEvent(SearchScreenEvents.SearchQueryCleared)
        }

        binding.searchEt.setOnEditorActionListener { v, actionId, event ->
            if ( actionId == EditorInfo.IME_ACTION_SEARCH ) {
                hideKeyboard(v)
                if ( binding.queriesRv.visibility == View.VISIBLE ) binding.queriesRv.visibility = View.GONE
                searchViewModel.onEvent( SearchScreenEvents.SearchClicked)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.searchEt.setOnFocusChangeListener { v, hasFocus ->
            if ( hasFocus ) {
                binding.queriesRv.visibility = View.VISIBLE
            }
        }

        binding.searchEt.setOnClickListener {
            if ( binding.queriesRv.visibility == View.GONE ) binding.queriesRv.visibility = View.VISIBLE
        }

    }

    private fun showLoadingIndicator() {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.refreshErrorIndicatorContainer.visibility = View.GONE
        binding.tvSearchResult.visibility = View.GONE
    }

    private fun showErrorIndicator() {
        binding.loadingIndicator.visibility = View.GONE
        binding.refreshErrorIndicatorContainer.visibility = View.VISIBLE
        binding.tvSearchResult.visibility = View.GONE
        binding.refreshRetryBtn.setOnClickListener {
            searchAdapter.refresh()
        }
    }

    private fun hideLoadingAndErrorIndicator() {
        binding.loadingIndicator.visibility = View.GONE
        binding.tvSearchResult.visibility = View.GONE
        binding.refreshErrorIndicatorContainer.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageClick(imageUrl: String) {
        findNavController().navigate(SearchFragmentDirections.searchFragmentToImageViewFragment(imageUrl))
    }

    override fun onQueryClicked(queryText: String) {
        hideKeyboard(requireView())
        searchViewModel.onEvent( SearchScreenEvents.RVQueryClicked(queryText) ) //here its trimmed already
        binding.queriesRv.visibility = View.GONE
    }

    override fun onQueryDeleted(searchQuery: SearchQuery) {
        searchViewModel.onEvent( SearchScreenEvents.SearchQueryDeletedFromCache(searchQuery) )
    }
}