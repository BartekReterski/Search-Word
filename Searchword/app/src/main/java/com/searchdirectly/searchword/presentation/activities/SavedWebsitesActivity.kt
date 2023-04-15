package com.searchdirectly.searchword.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.ActivitySavedWebsitesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedWebsitesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedWebsitesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedWebsitesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Saved web links"
        setupMenu()
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