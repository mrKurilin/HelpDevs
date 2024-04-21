package ru.mrkurilin.helpDevs.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.mrkurilin.helpDevs.data.remote.interceptors.NetworkCacheInterceptor
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import java.util.concurrent.TimeUnit

private const val CONNECT_TIME = 5000L
private const val WRITE_TIME = 5000L
private const val READE_TIME = 5000L
private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideOkHttpClient(
        cacheInterceptor: NetworkCacheInterceptor,
        @ApplicationContext
        context: Context,
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, CACHE_SIZE))
        .addInterceptor(cacheInterceptor)
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
        .readTimeout(READE_TIME, TimeUnit.SECONDS)
        .build()
}
