package com.searchdirectly.searchword.domain.di

import android.content.Context
import com.searchdirectly.searchword.domain.data.interfaces.SearchWordInterface
import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSearchWordRepository(@ApplicationContext appContext: Context): SearchWordInterface =
        SearchWordRepository(context = appContext)
}