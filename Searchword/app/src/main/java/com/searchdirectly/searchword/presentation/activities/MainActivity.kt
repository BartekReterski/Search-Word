package com.searchdirectly.searchword.presentation.activities


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.ActivityMainBinding
import com.searchdirectly.searchword.presentation.fragments.BookmarkFragment
import com.searchdirectly.searchword.presentation.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupLayout()
    }

    private fun setupLayout() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        loadFragment(HomeFragment())
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.action_bookmark -> {
                    loadFragment(BookmarkFragment())
                    true
                }
                R.id.action_share -> {
                    true
                }
                R.id.action_save -> {
                    Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show()
                   // binding.bottomNavigation.menu.findItem(R.id.action_bookmark).isVisible = true
                    true
                }
                R.id.action_refresh -> {
                    //binding.bottomNavigation.menu.findItem(R.id.action_bookmark).isVisible = false
                    true
                }
                else -> {
                    Toast.makeText(
                        applicationContext,
                        "Operation not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}

