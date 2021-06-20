package com.vijay.app.di

import android.content.Context
import com.vijay.app.App
import dagger.Module
import dagger.Provides

@Module
class AppDiModule {

    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }

}