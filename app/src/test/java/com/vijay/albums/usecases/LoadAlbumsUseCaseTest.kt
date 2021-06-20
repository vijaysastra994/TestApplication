package com.vijay.albums.usecases

import com.vijay.albums.usecases.repository.LoadAlbumsRepository
import com.vijay.albums.usecases.repository.db.ItemAlbumModel
import com.vijay.albums.utils.CoroutineTestExtension
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


class LoadAlbumsUseCaseTest {

    @ExperimentalCoroutinesApi
    @RegisterExtension
    @JvmField
    val coroutineTestExtension = CoroutineTestExtension()

    private val itemAlbumModel = ItemAlbumModel(1, "title")
    private val itemAlbumModels = listOf(itemAlbumModel)
    private val loadAlbumsRepository: LoadAlbumsRepository = mock()

    @ExperimentalCoroutinesApi
    @Test
    fun run() = coroutineTestExtension.testDispatcher.runBlockingTest {
        whenever(loadAlbumsRepository.albums(any())).thenReturn(MutableStateFlow(itemAlbumModels))

        val loadAlbumsUseCase = LoadAlbumsUseCase(loadAlbumsRepository)

        val result = loadAlbumsUseCase.run(this).first()

        assertEquals(itemAlbumModels, result)
    }

}