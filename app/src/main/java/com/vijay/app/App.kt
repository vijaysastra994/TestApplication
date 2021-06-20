package com.vijay.app

import android.app.Application
import com.vijay.app.di.AlbumsApp

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    open fun initDi() {
        AlbumsApp.init(this)
    }
}