package com.searchdirectly.searchword.domain.di

import com.searchdirectly.searchword.domain.data.SearchWordInterface
import com.searchdirectly.searchword.domain.data.SearchWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSearchWordRepository():SearchWordInterface=SearchWordRepository()
}