package com.vijay.albums.di

import com.vijay.albums.usecases.repository.LoadAlbumsRepository
import com.vijay.albums.usecases.repository.LoadAlbumsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [LoadItemsProviderModule::class])
abstract class LoadAlbumsModule {

    @Binds
    abstract fun loadAlbumsRepository(loadAlbumsRepositoryImpl: LoadAlbumsRepositoryImpl): LoadAlbumsRepository
}