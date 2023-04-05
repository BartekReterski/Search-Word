package com.searchdirectly.searchword.presentation.activities


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.ActivityMainBinding
import com.searchdirectly.searchword.presentation.fragments.BookmarkFragment
import com.searchdirectly.searchword.presentation.fragments.HomeFragment
import com.searchdirectly.searchword.presentation.fragments.RecentSearchFragment


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
                R.id.action_recent -> {
                    loadFragment(RecentSearchFragment())
                    true
                }
                R.id.action_bookmark -> {
                    loadFragment(BookmarkFragment())
                    true
                }
                else -> {
                    TODO()
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
           menuInflater.inflate(R.menu.main_menu, menu)
           val search = menu?.findItem(R.id.action_search)
           val searchView = search?.actionView as SearchView
           searchView.queryHint = "Type phrase and choose website to search"
           searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
               override fun onQueryTextSubmit(query: String?): Boolean {
                   //show dialog(about search phrase), if text is not null and chipItem is selected
                   return false
               }

               override fun onQueryTextChange(newText: String?): Boolean {
                   // adapter.filter.filter(newText)
                   return true
               }
           })
           return super.onCreateOptionsMenu(menu)
       }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_save -> {
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

