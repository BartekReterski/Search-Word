package com.searchdirectly.searchword.rules

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

interface InstantExecutor {

    @get:Rule
    val rule
        get() = InstantTaskExecutorRule()
}