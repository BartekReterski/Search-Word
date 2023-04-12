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
import com.searchdirectly.searchword.presentation.fragments.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedWebsitesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedWebsitesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedWebsitesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Saved weblinks"
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
                        sendFlagToHomeFragment()
                        finish()
                        true
                    }
                    else -> false
                }
        }
        },this, Lifecycle.State.RESUMED)
    }

    private fun sendFlagToHomeFragment(){
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putBoolean("SavedWebsiteActivity",true)
        homeFragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, homeFragment)
    }
}