package com.vijay.albums.di

import android.content.Context
import androidx.room.Room
import com.vijay.albums.usecases.repository.AlbumsRemoteDataStore
import com.vijay.albums.usecases.repository.db.AlbumsDataBase
import com.vijay.albums.usecases.repository.db.AlbumsLocalDataStore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit

@Module
class LoadItemsProviderModule {

    companion object {
        const val ALBUMS_DATABASE_NAME = "albumsDataBase"
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideAlbumsDataBase(context: Context): AlbumsDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlbumsDataBase::class.java,
            ALBUMS_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideAlbumsDao(albumsDataBase: AlbumsDataBase): AlbumsLocalDataStore {
        return albumsDataBase.albumsDao()
    }

    @Provides
    fun provideMovieLoaderRemoteDataStore(retrofit: Retrofit): AlbumsRemoteDataStore {
        return retrofit.create(AlbumsRemoteDataStore::class.java)
    }
}