package com.vijay.app.di

import com.vijay.albums.di.LoadAlbumsModule
import com.vijay.app.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ViewModelMapperModule::class,
        ViewBuilderModule::class,
        LoadAlbumsModule::class,
        AppDiModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    val androidInjector: DispatchingAndroidInjector<Any>

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun albumsApp(app: App): Builder

        fun build(): ApplicationComponent
    }
}

object AlbumsApp {

    private lateinit var applicationComponent: ApplicationComponent

    @JvmStatic
    fun init(app: App) {
        applicationComponent = DaggerApplicationComponent.builder()
            .albumsApp(app)
            .build()
        inject(app)
    }

    fun inject(resource: Any) {
        applicationComponent.androidInjector.inject(resource)
    }

}
