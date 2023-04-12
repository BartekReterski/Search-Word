package com.searchdirectly.searchword.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.searchdirectly.searchword.databinding.ActivitySavedWebsitesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedWebsitesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedWebsitesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedWebsitesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Saved weblinks"
    }
}