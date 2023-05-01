package com.searchdirectly.searchword.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.searchdirectly.searchword.BuildConfig
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.FragmentHomeBinding
import com.searchdirectly.searchword.domain.model.room.SavedLinks
import com.searchdirectly.searchword.domain.model.savestate.SaveState
import com.searchdirectly.searchword.domain.model.websites.WebSites
import com.searchdirectly.searchword.presentation.activities.SavedWebsitesActivity
import com.searchdirectly.searchword.presentation.uistates.savestate.SaveStateHandle
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
    private lateinit var searchView: SearchView

    private val viewModel: WebSiteViewModel by viewModels()
    private val viewModelSavedLinks: SavedLinksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity?.title = getString(R.string.HomeFragmentTitle)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        try {
            getSaveState()
            if (finalUrl != "null" || querySearch != "null") {
                binding.webview.loadUrl(finalUrl!!)
            }

        } catch (e: java.lang.Exception) {
            Log.e(
                "Save_State_Handle_Error", "Error regarding to save temporary url and query"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        saveSaveState()
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
                searchView = search?.actionView as SearchView
                configSearchView(search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        querySearch = query
                        //resetSharedPreferences()
                        if (querySearch.isNullOrEmpty().not() && selectedChips()) {
                            viewModel.getWebsiteDataByName(savedCurrentSiteName)
                            observeViewModel()
                        } else {
                            Toast.makeText(
                                context, getString(R.string.what_to_do_info), Toast.LENGTH_LONG
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
                        aboutAppAndRateDialog()
                        true
                    }
                    R.id.action_bookmark -> {
                        val intent = Intent(activity, SavedWebsitesActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun configSearchView(search: MenuItem) {
        if (querySearch != "null" && querySearch!!.isEmpty().not()) {
            searchView.queryHint = getString(R.string.search_query_hint)
            search.expandActionView()
            searchView.setQuery(querySearch, false)
        }
    }

    private fun observeViewModel() {
        //observe Website data from repository
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.webSitesUiState.distinctUntilChangedBy { it.listState }
                    .map { it.listState }.collectLatest { webState ->
                        when (webState) {
                            is WebState.Success -> {
                                val website = webState.webSite
                                openWebViewBasedOnUrl(website, querySearch)
                            }
                            is WebState.Error -> {
                                Log.e("Error state", "Passing website URL")
                            }
                            is WebState.NoInternetConnection -> {
                                Toast.makeText(
                                    context, R.string.no_internet_info, Toast.LENGTH_SHORT
                                ).show()
                            }
                            is WebState.Loading -> {}
                            is WebState.Empty -> {}
                        }
                    }
            }
        }
        //observe saved state url
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveStateHandleUiState.distinctUntilChangedBy { it.showedSaveStateHandleAddedMessage }
                    .collectLatest {
                        if (it.showedSaveStateHandleAddedMessage) {
                            viewModel.addedSavedSateInfo()
                        }
                    }

            }
        }
        //observe getting saved url from Saved State
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveStateHandleUiState.distinctUntilChangedBy { it.saveStateHandle }
                    .map { it.saveStateHandle }.collectLatest { savedState ->
                        when (savedState) {
                            is SaveStateHandle.Success -> {
                                val savedUrl = savedState.saveState.hyperLink
                                val savedQuery = savedState.saveState.queryValue
                                if (savedUrl.isNullOrEmpty().not()) {
                                    finalUrl = savedUrl
                                    querySearch = savedQuery
                                }
                            }
                            is SaveStateHandle.Error -> {
                                Log.e("Error state", "Getting URL from shared preferences")
                            }
                            is SaveStateHandle.Loading -> {}
                            is SaveStateHandle.Empty -> {}
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
                            Toast.makeText(
                                context, getString(R.string.saved_item_info), Toast.LENGTH_SHORT
                            ).show()
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
        binding.webview.webViewClient = WebViewClient()
        binding.webview.apply {
            loadUrl(finalUrl!!)
            settings.javaScriptEnabled = true
        }
        binding.webview.webChromeClient = WebChromeClient()
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {

        override fun onPermissionRequest(request: PermissionRequest) {
            val resources = request.resources
            for (i in resources?.indices!!) {
                if (PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID == resources[i]) {
                    request.grant(resources)
                    return
                }
            }
            super.onPermissionRequest(request)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            binding.progressBarHorizontal.visibility = View.VISIBLE
            binding.progressBarHorizontal.setProgress(newProgress, true)
            if (newProgress == 100) {
                binding.progressBarHorizontal.visibility = View.GONE
            }
        }
    }

    // Overriding WebViewClient functions
    inner class WebViewClient : android.webkit.WebViewClient() {

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest
        ): Boolean {
            val uri = request.url
            view?.loadUrl(uri.toString())
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            manipulateLayoutVisibilityOnPageFinished(view)
        }
    }

    private fun manipulateLayoutVisibilityOnPageFinished(view: WebView) {
        binding.progressBarHorizontal.visibility = View.GONE
        binding.webview.visibility = View.VISIBLE
        binding.textViewHelper.visibility = View.GONE
        binding.imageViewHelper.visibility = View.GONE
        if (binding.webview.isVisible) {
            showBottomNavigation(true)
        }
        view.hideSoftInput()
    }

    private fun showBottomNavigation(show: Boolean) {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        if (show) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
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
                        resetSaveState()
                    }
                    resetSaveState()
                    observeViewModel()
                    savedCurrentSiteName = selectedChipText
                    viewModel.getWebsiteDataByName(selectedChipText)
                } else {
                    Toast.makeText(
                        context, getString(R.string.what_to_do_info), Toast.LENGTH_LONG
                    ).show()
                    binding.chipGroup.clearCheck()
                }
            } catch (e: java.lang.Exception) {
                Log.e("Error catch", e.message.toString())
            }
        }
    }

    private fun resetSaveState() {
        viewModel.saveSavedState(SaveState("", ""))
        viewModel.getSavedState()
    }

    private fun saveSaveState() {
        viewModel.saveSavedState(SaveState(binding.webview.url, searchView.query.toString()))
    }

    private fun getSaveState() {
        viewModel.getSavedState()
    }

    //hide keyboard
    private fun View.hideSoftInput() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun closeWebView(context: Context) {
        if (binding.webview.isVisible) {
            view?.hideSoftInput()
            binding.webview.visibility = View.GONE
            binding.textViewHelper.visibility = View.VISIBLE
            binding.imageViewHelper.visibility = View.VISIBLE
            showBottomNavigation(false)
        }
    }

    fun refreshWebView(context: Context) {
        val url = binding.webview.url
        if (binding.webview.isVisible && url.isNullOrEmpty().not()) {
            binding.progressBarHorizontal.visibility = View.VISIBLE
            binding.webview.loadUrl(url!!)
        } else {
            Toast.makeText(context, R.string.no_internet_info, Toast.LENGTH_SHORT).show()
        }
    }

    fun shareUrl(context: Context) {
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
                context, R.string.share_info, Toast.LENGTH_LONG
            ).show()
        }
    }

    fun saveLinkInDatabase(context: Context) {
        if (finalUrl.isNullOrEmpty().not() && binding.webview.isVisible) {
            val currentWebLink = binding.webview.url
            val currentLinkTitle = binding.webview.title
            val savedTime = DateFormat.getInstance().format(System.currentTimeMillis())
            //val favicon = binding.webview.favicon
            viewModelSavedLinks.saveWebLink(
                SavedLinks(
                    title = currentLinkTitle!!,
                    hyperLink = currentWebLink!!,
                    creationTime = savedTime
                )
            )
        } else {
            Toast.makeText(
                context, R.string.save_info, Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openLinkInBrowser(context: Context) {
        if (finalUrl.isNullOrEmpty().not() && binding.webview.isVisible) {
            val urlIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(binding.webview.url)
            )
            startActivity(urlIntent)
        } else {
            Toast.makeText(
                context, R.string.open_in_browser, Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun aboutAppAndRateDialog() {

        val dialogBuilder =
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
        val versionCode = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME

        dialogBuilder.setMessage("Application version: $versionName$versionCode\n\nIf you enjoy using the app would you mind taking a moment to rate it?")
            .setCancelable(false).setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.cancel()
            }.setNeutralButton(getString(R.string.rate_app_info)) { dialog, _ ->

                val appPackage = requireActivity().packageName
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")
                        )
                    )
                } catch (e: java.lang.Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                        )
                    )
                }
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.app_name)
        alert.show()
    }

    //device - back button
    private fun preventBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backArrowButton(context = context!!)
                }
            })
    }

    //icon back button on bottom navigation bar
    fun backArrowButton(context: Context) {
        if (binding.webview.canGoBack() && binding.webview.isVisible) binding.webview.goBack()
        else if (!binding.webview.canGoBack() || binding.webview.isVisible) {
            val builder =
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
            builder.setTitle(getString(R.string.exit_app))
            builder.setMessage(getString(R.string.exit_app_confirm))
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                requireActivity().finish()
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
            builder.show()
        }
    }

    //check if at least one chip is selected
    private fun selectedChips(): Boolean {
        val ids: List<Int> = binding.chipGroup.checkedChipIds
        for (id in ids) {
            val chip: Chip = binding.chipGroup.findViewById(id)
        }
        Log.e("List is not empty", ids.size.toString())
        return ids.isNotEmpty()
    }
}
