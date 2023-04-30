package com.searchdirectly.searchword.presentation.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.ActivityMainBinding
import com.searchdirectly.searchword.presentation.fragments.HomeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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
                R.id.action_home_bottom_nav -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.action_back_bottom_nav -> {
                    homeFragment().backArrowButton(this)
                    true
                }
                R.id.action_close_browser_bottom_nav -> {
                    homeFragment().closeWebView(this)
                    true
                }
                R.id.action_refresh_bottom_nav -> {
                    homeFragment().refreshWebView(this)
                    true
                }
                R.id.action_more_bottom_nav -> {
                    popupMenuSetup()
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

    private fun homeFragment(): HomeFragment {
        return supportFragmentManager.findFragmentById(R.id.frameLayout) as HomeFragment
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun popupMenuSetup() {
        val wrapper: Context = ContextThemeWrapper(this, R.style.CustomPopUpStyle)
        val popupMenu = PopupMenu(wrapper, binding.bottomNavigation, Gravity.END)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    homeFragment().shareUrl(this)
                    true
                }
                R.id.action_open_browser -> {
                    homeFragment().openLinkInBrowser(this)
                    true
                }
                R.id.action_save -> {
                    homeFragment().saveLinkInDatabase(this)
                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.popup_bottom_navigation_menu)

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

}

