package com.searchdirectly.searchword.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.FragmentHomeBinding
import com.searchdirectly.searchword.domain.model.SavedLinks
import com.searchdirectly.searchword.domain.model.WebSites
import com.searchdirectly.searchword.presentation.uistates.preferences.SharedPreferencesState
import com.searchdirectly.searchword.presentation.uistates.websites.WebState
import com.searchdirectly.searchword.presentation.viewmodels.room.SavedLinksViewModel
import com.searchdirectly.searchword.presentation.viewmodels.websites.WebSiteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DateFormat


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var querySearch: String? = ""
    private var savedCurrentSiteName: String = ""
    private var finalUrl: String? = ""

    private val viewModel: WebSiteViewModel by viewModels()
    private val viewModelSavedLinks: SavedLinksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "Search word"
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        try {
            viewModel.getSavedSharedPreferencesUrl()
            binding.webview.loadUrl(finalUrl!!)

        } catch (e: java.lang.Exception) {
            Log.e(
                "Shared_Preferences_Error",
                "Error regarding to save url address in Shared Preferences"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveSharedPreferencesUrl(binding.webview.url!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupChips()
        observeViewModel()
        preventBackButton()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                val search = menu.findItem(R.id.action_search)
                val searchView = search?.actionView as SearchView
                searchView.queryHint = getString(R.string.search_query_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        querySearch = query
                        if (querySearch.isNullOrEmpty().not() && selectedChips()) {
                            viewModel.getWebsiteDataByName(savedCurrentSiteName)
                            observeViewModel()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.what_to_do_info),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        querySearch = newText
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_about -> {
                        true
                    }
                    R.id.action_share -> {
                        shareUrl()
                        true
                    }
                    R.id.action_save -> {
                        saveLinkInDatabase()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeViewModel() {
        //observe Website data from repository
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.webSitesUiState.distinctUntilChangedBy { it.listState }
                    .map { it.listState }
                    .collectLatest { webState ->
                        when (webState) {
                            is WebState.Success -> {
                                val website = webState.webSite
//                                Toast.makeText(
//                                    context,
//                                    website?.siteName + website?.url + website?.queryUrl,
//                                    Toast.LENGTH_LONG
//                                ).show()
                                openWebViewBasedOnUrl(website, querySearch)
                            }
                            is WebState.Error -> {
                                Log.e("Error state", "Passing website URL")
                            }
                            is WebState.Loading -> {}
                            is WebState.Empty -> {}
                        }
                    }
            }
        }
        //observe saved url to shared preferences
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedPreferencesUiState.distinctUntilChangedBy { it.showedSharedPreferencesAddedMessage }
                    .collectLatest {
                        if (it.showedSharedPreferencesAddedMessage) {
                            // Toast.makeText(context, "Saved SP", Toast.LENGTH_SHORT).show()
                            viewModel.addedSharedPreferencesMessageInfo()
                        }
                    }

            }
        }
        //observe getting url from shared preferences
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedPreferencesUiState.distinctUntilChangedBy { it.sharedPreferenceState }
                    .map { it.sharedPreferenceState }
                    .collectLatest { sharedPreferences ->
                        when (sharedPreferences) {
                            is SharedPreferencesState.Success -> {
                                val savedUrl = sharedPreferences.url
                                if (savedUrl.isNullOrEmpty().not()) {
                                    finalUrl = savedUrl
                                    //binding.webview.loadUrl(savedUrl!!)
                                }
//                                Toast.makeText(
//                                    context,
//                                    savedUrl,
//                                    Toast.LENGTH_LONG
//                                ).show()
                            }
                            is SharedPreferencesState.Error -> {
                                Log.e("Error state", "Getting URL from shared preferences")
                            }
                            is SharedPreferencesState.Loading -> {}
                            is SharedPreferencesState.Empty -> {}
                        }
                    }
            }
        }
        //observe adding link to database
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelSavedLinks.roomLinkUiState.distinctUntilChangedBy { it.showedAddedMessage }
                    .collectLatest {
                        if (it.showedAddedMessage) {
                            Toast.makeText(context, getString(R.string.saved_item_info), Toast.LENGTH_SHORT).show()
                            viewModelSavedLinks.addedMessageInfo()
                        }
                    }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebViewBasedOnUrl(webSites: WebSites?, querySearch: String?) {
        val url = webSites?.url
        val queryUrl = webSites?.queryUrl + querySearch
        finalUrl = url + queryUrl
        if (isNetworkAvailable(requireContext())) {
            binding.webview.webViewClient = WebViewClient()
            binding.webview.apply {
                loadUrl(finalUrl!!)
                settings.javaScriptEnabled = true
            }
            binding.webview.webChromeClient = WebChromeClient()
        } else {
            Toast.makeText(context, R.string.no_internet_info, Toast.LENGTH_LONG).show()
        }
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            binding.progressBarHorizontal.setProgress(newProgress, true)
        }
    }

    // Overriding WebViewClient functions
    inner class WebViewClient : android.webkit.WebViewClient() {

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            binding.progressBarHorizontal.visibility = View.VISIBLE
            view.loadUrl(url)
            return false
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest
        ): Boolean {
            val uri = request.url
            binding.progressBarHorizontal.visibility = View.VISIBLE
            view?.loadUrl(uri.toString())
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progressBarHorizontal.visibility = View.GONE
            binding.webview.visibility = View.VISIBLE
            binding.textViewHelper.visibility = View.GONE
            binding.imageViewHelper.visibility = View.GONE
            view.hideSoftInput()
        }
    }

    private fun setupChips() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, _ ->
            try {
                if (querySearch.isNullOrEmpty().not()) {
                    val selectedChipText =
                        binding.chipGroup.findViewById<Chip>(binding.chipGroup.checkedChipId).text.toString()
                    if (selectedChipText == savedCurrentSiteName) {
                        observeViewModel()
                    }
                    savedCurrentSiteName = selectedChipText
                    viewModel.getWebsiteDataByName(selectedChipText)
                    binding.progressBarHorizontal.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.what_to_do_info),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.chipGroup.clearCheck()
                }
            } catch (e: java.lang.Exception) {
                Log.e("Error catch", e.message.toString())
            }
        }
    }

    //hide keyboard
    fun View.hideSoftInput() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun closeWebView(context: Context) {
        binding.webview.visibility = View.GONE
        binding.textViewHelper.visibility = View.VISIBLE
        binding.imageViewHelper.visibility = View.VISIBLE
    }

    fun refreshWebView(context: Context) {
        if (binding.webview.isVisible) {
            val url = binding.webview.url
            binding.progressBarHorizontal.visibility = View.VISIBLE
            binding.webview.loadUrl(url!!)
        }
    }

    fun shareUrl() {
        if (finalUrl.isNullOrEmpty().not() && binding.webview.isVisible) {
            val shareUrl = binding.webview.url
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareUrl)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            refreshWebView(requireContext())
        } else {
            Toast.makeText(
                context,
                R.string.share_info,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveLinkInDatabase() {
        if (finalUrl.isNullOrEmpty().not() && binding.webview.isVisible) {
            val currentWebLink = binding.webview.url
            val currentLinkTitle = binding.webview.title
            val savedTime = DateFormat.getInstance().format(System.currentTimeMillis())
            viewModelSavedLinks.saveWebLink(
                SavedLinks(
                    title = currentLinkTitle!!,
                    hyperLink = currentWebLink!!,
                    creationTime = savedTime
                )
            )
        }
    }

    //device - back button
    private fun preventBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webview.canGoBack()) binding.webview.goBack()
                }
            })
    }

    //icon back button on bottom navigation bar
    fun backArrowButton(context: Context) {
        if (binding.webview.canGoBack()) binding.webview.goBack()
    }

    private fun selectedChips(): Boolean {
        val ids: List<Int> = binding.chipGroup.checkedChipIds
        for (id in ids) {
            val chip: Chip = binding.chipGroup.findViewById(id)
        }
        Log.e("List is not empty", ids.size.toString())
        return ids.isNotEmpty()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}
