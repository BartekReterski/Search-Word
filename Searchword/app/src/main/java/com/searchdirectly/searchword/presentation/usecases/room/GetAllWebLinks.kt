package com.searchdirectly.searchword.presentation.usecases.room

import com.searchdirectly.searchword.domain.data.repositories.RoomRepository
import com.searchdirectly.searchword.domain.model.room.SavedLinks
import javax.inject.Inject

class GetAllWebLinks @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(): Result<List<SavedLinks>> = try {
        Result.success(roomRepository.getAllLinks())
    } catch (e: Exception) {
        Result.failure(e)
    }
}