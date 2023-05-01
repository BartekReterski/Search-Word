package com.searchdirectly.searchword.presentation.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.searchdirectly.searchword.presentation.uistates.room.RoomLinksListState
import com.searchdirectly.searchword.presentation.usecases.room.AddWebLink
import com.searchdirectly.searchword.presentation.usecases.room.GetAllWebLinks
import com.searchdirectly.searchword.presentation.usecases.room.RemoveLink
import com.searchdirectly.searchword.presentation.usecases.savestate.GetSaveStateData
import com.searchdirectly.searchword.presentation.usecases.savestate.SaveStateData
import com.searchdirectly.searchword.presentation.usecases.websites.GetWebSiteByName
import com.searchdirectly.searchword.presentation.viewmodels.room.SavedLinksViewModel
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
import app.cash.turbine.test

@ExperimentalCoroutinesApi
class SavedLinksViewModelTest : InstantExecutor {

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var savedLinksViewModel: SavedLinksViewModel

    @MockK(relaxed = true)
    private lateinit var getAllWebLinks: GetAllWebLinks

    @MockK(relaxed = true)
    private lateinit var removeLink: RemoveLink

    @MockK(relaxed = true)
    private lateinit var addWebLink: AddWebLink

    private val correctWebsiteName: String = "Bing"
    private val wrongWebsiteName: String = "Onet"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initWebsiteViewModel()
    }

    private fun initWebsiteViewModel() {
        savedLinksViewModel = SavedLinksViewModel(
            getAllWebLinks = getAllWebLinks,
            removeLink = removeLink,
            addWebLink = addWebLink
        )
        @Test
        fun `save web link`() = runTest {
            savedLinksViewModel.roomLinksListUiState.test {
                Assert.assertEquals(RoomLinksListState.Empty, expectMostRecentItem())
            }

        }
    }
}