package com.searchdirectly.searchword.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.ActivitySavedWebsitesBinding
import com.searchdirectly.searchword.domain.model.SavedLinks
import com.searchdirectly.searchword.presentation.adapters.SavedLinkListAdapter
import com.searchdirectly.searchword.presentation.uistates.room.RoomLinksListState
import com.searchdirectly.searchword.presentation.viewmodels.room.SavedLinksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedWebsitesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedWebsitesBinding
    private val savedWebLinksAdapter = SavedLinkListAdapter(arrayListOf())
    private val viewModelSavedLinks: SavedLinksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedWebsitesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = getString(R.string.SavedWebsiteActivityTitle)
        observeViewModel()
        setupMenu()
        setupLayout()
    }

    private fun setupLayout() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = savedWebLinksAdapter
        }
    }

    private fun observeViewModel() {
        //observe getting list of saved links from Room database
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelSavedLinks.roomLinksListUiState.distinctUntilChangedBy { it.listLinksState }
                    .map { it.listLinksState }
                    .collectLatest { listState ->
                        when (listState) {
                            is RoomLinksListState.Success -> {
                                val listOfSavedLinks = listState.linkList
                                if (listOfSavedLinks.isEmpty().not()) {
                                    savedWebLinksAdapter.updateHyperLink(listOfSavedLinks)
                                    showLayoutItems(true)
                                } else {
                                    showLayoutItems(false)
                                }
                            }
                            is RoomLinksListState.Error -> {
                                Log.e("Error room list", "Database saved weblinks")
                            }
                            is RoomLinksListState.Loading -> {}
                            is RoomLinksListState.Empty -> {}
                        }
                    }
            }
        }

        //observe removing link from database
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelSavedLinks.roomLinkUiState.distinctUntilChangedBy { it.showedRemovedMessage }
                    .collectLatest {
                        if (it.showedRemovedMessage) {
                            Toast.makeText(
                                this@SavedWebsitesActivity,
                                getString(R.string.link_removed),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModelSavedLinks.removedMessageInfo()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelSavedLinks.getSavedWebLinks()
    }

    fun removeLinkFromDatabase(savedLinks: SavedLinks) {
        viewModelSavedLinks.removeWebLink(savedLinks)
        observeViewModel()
    }

    private fun showLayoutItems(show: Boolean) {
        if (show) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.imageViewHelperSavedWebsiteActivity.visibility = View.GONE
            binding.textViewHelperSavedWebsiteActivity.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.imageViewHelperSavedWebsiteActivity.visibility = View.VISIBLE
            binding.textViewHelperSavedWebsiteActivity.visibility = View.VISIBLE
        }
    }

    private fun setupMenu() {
        this.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.saved_website_link_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }
}