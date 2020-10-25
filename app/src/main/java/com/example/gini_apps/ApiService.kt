package com.example.gini_apps

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File


interface ApiService {

    @GET("8wJzytQX")
    fun getData(): Single<DataResponse>

    companion object{
        private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        private var interceptor = HttpLoggingInterceptor()


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun initApiService(context: Context): ApiService {


            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(
                interceptor
            )

            httpClient.addInterceptor {

                val original: Request = it.request()
                val originalHttpUrl: HttpUrl = original.url()

                val url: HttpUrl = originalHttpUrl.newBuilder()
                    .build()

                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)

                val request: Request = requestBuilder.build()
                it.proceed(request)
            }


            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?


            val builder = NetworkRequest.Builder()

            connectivityManager!!.registerNetworkCallback(
                builder.build(),
                object : NetworkCallback() {
                    /**
                     * @param network
                     */
                    override fun onAvailable(network: Network) {

                        httpClient.addInterceptor{

                            var original:Response = it.proceed(it.request())
                            var maxAge = 60
                            original.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).build()
                            return@addInterceptor original
                        }
                    }

                    /**
                     * @param network
                     */
                    override fun onLost(network: Network) {
                        httpClient.addInterceptor{
                            val maxStale = 60 * 60 * 24 * 28
                            var original:Response = it.proceed(it.request())
                            original.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                                .build()
                        }
                    }
                }
            )


            val httpCacheDirectory = File(context.getCacheDir(), "responses")
            val cacheSize = 10 * 1024 * 1024

            val cache = Cache(httpCacheDirectory, cacheSize.toLong())

            httpClient.cache(cache)

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://pastebin.com/raw/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}