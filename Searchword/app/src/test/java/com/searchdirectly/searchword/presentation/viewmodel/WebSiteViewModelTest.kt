package com.searchdirectly.searchword.presentation.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.searchdirectly.searchword.presentation.uistates.websites.WebState
import com.searchdirectly.searchword.presentation.usecases.savestate.GetSaveStateData
import com.searchdirectly.searchword.presentation.usecases.savestate.SaveStateData
import com.searchdirectly.searchword.presentation.usecases.websites.GetWebSiteByName
import com.searchdirectly.searchword.presentation.viewmodels.websites.WebSiteViewModel
import com.searchdirectly.searchword.rules.CoroutineTestRule
import com.searchdirectly.searchword.rules.InstantExecutor
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WebSiteViewModelTest : InstantExecutor {

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var webSiteViewModel: WebSiteViewModel

    @MockK(relaxed = true)
    private lateinit var savedStateData: SaveStateData

    @MockK(relaxed = true)
    private lateinit var application: Application

    @MockK(relaxed = true)
    private lateinit var getWebSiteByName: GetWebSiteByName

    @MockK(relaxed = true)
    private lateinit var getSavedStateData: GetSaveStateData

    private val correctWebsiteName: String = "Bing"
    private val wrongWebsiteName: String = "Onet"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initWebsiteViewModel()
    }

    private fun initWebsiteViewModel() {
        webSiteViewModel = WebSiteViewModel(
            saveStateData = savedStateData,
            application = application,
            getWebSiteByName = getWebSiteByName,
            getSaveStateData = getSavedStateData
        )
    }

//    @Test
//    fun `when website name is valid, state should be success`() =
//        runTest {
//            webSiteViewModel.webSitesUiState.test {
//                Assert.assertEquals(WebState.Empty, expectMostRecentItem().listState)
//                webSiteViewModel.getWebsiteDataByName(correctWebsiteName)
//                Assert.assertEquals(WebState.Success(webSite = null), expectMostRecentItem().listState)
//                cancelAndIgnoreRemainingEvents()
//            }
//
//        }
}
