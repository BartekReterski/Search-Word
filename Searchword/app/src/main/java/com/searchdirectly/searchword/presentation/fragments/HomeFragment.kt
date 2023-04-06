package com.searchdirectly.searchword.presentation.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.FragmentHomeBinding
import com.searchdirectly.searchword.presentation.uistates.WebState
import com.searchdirectly.searchword.presentation.viewmodels.WebSiteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //reference to ViewModel which is connected to this fragment
    private val viewModel: WebSiteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "Search word"
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWebSiteInfoByName("Bing")
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.webSitesUiState.distinctUntilChangedBy { it.listState }.map { it.listState }
                .collectLatest { webState ->
                    when (webState) {
                        is WebState.Success -> {
                            val website = webState.webSite
                            Toast.makeText(
                                context,
                                website?.siteName + website?.url + website?.queryUrl,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is WebState.Error -> {}
                        is WebState.Loading -> {}
                        is WebState.Empty -> {}
                    }
                }
        }
    }

    private fun openWebViewBasedOnLinks(){

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Type phrase and choose website to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // adapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about_app -> {
                Toast.makeText(requireContext(), "Item 1 selected", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}