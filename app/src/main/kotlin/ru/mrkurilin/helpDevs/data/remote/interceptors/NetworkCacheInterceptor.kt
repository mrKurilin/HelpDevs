package ru.mrkurilin.helpDevs.data.remote.interceptors


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import ru.mrkurilin.helpDevs.di.qualifiers.ApplicationContext
import ru.mrkurilin.helpDevs.di.scopes.AppScope
import javax.inject.Inject

@AppScope
class NetworkCacheInterceptor @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : Interceptor {

    companion object {
        private const val CACHE_NAME = "Cache-Control"
        private const val HOUR = 3600
        private const val MINUTES = 60
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (isNetworkAvailable(context)) {
            request.newBuilder()
                .header(CACHE_NAME, "public, max-age=$MINUTES")
                .build()
        } else {
            request.newBuilder()
                .header(CACHE_NAME, "public, only-if-cached, max-stale=$HOUR")
                .build()
        }

        return chain.proceed(request)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return (
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        ||
                        actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                )
    }
}
