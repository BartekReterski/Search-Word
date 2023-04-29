package com.searchdirectly.searchword.domain.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.searchdirectly.searchword.domain.data.interfaces.SearchWordInterface
import com.searchdirectly.searchword.domain.data.repositories.RoomDataSource
import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSearchWordRepository(
        @ApplicationContext appContext: Context,
        savedStateHandle: SavedStateHandle
    ): SearchWordInterface =
        SearchWordRepository(context = appContext, state = savedStateHandle)

    @Provides
    @Singleton
    fun provideRoomRepository(app: Application) = RoomRepository(RoomDataSource(app))
}