package com.searchdirectly.searchword.presentation.usecases.network

import com.searchdirectly.searchword.domain.data.repositories.SearchWordRepository
import javax.inject.Inject

class IsNetworkConnected @Inject constructor(private val searchWordRepository: SearchWordRepository) {

    suspend operator fun invoke(): Boolean = searchWordRepository.isNetworkAvailable()
}