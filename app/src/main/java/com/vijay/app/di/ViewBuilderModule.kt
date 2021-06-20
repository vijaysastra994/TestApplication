package com.vijay.app.di

import com.vijay.albums.AlbumsFragment
import com.vijay.app.App
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewBuilderModule {

    @ContributesAndroidInjector
    abstract fun bindsAlbumsFragment(): AlbumsFragment

    @ContributesAndroidInjector
    abstract fun bindsApp(): App
}
