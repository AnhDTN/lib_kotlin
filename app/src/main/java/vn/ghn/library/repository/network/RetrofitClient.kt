package vn.ghn.library.repository.network

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitClient @Inject constructor()  {

    fun initRetrofit(baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}, authenticator: Authenticator? = null, isDebug: Boolean): Retrofit.Builder {
        val newClient = getRetrofitClient(authenticator, isDebug)
        newClient.block()
        return retrofit
            .client(newClient.build())
            .baseUrl(baseURL)
    }

    private val retrofit: Retrofit.Builder
        get() = Retrofit.Builder().also {
            it.addConverterFactory(GsonConverterFactory.create())
        }

    private fun getRetrofitClient(authenticator: Authenticator? = null, isDebug: Boolean = false): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                authenticator?.let { client.authenticator(it) }
                if (isDebug) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
    }

    fun authInterceptor(token: String?): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", String.format("Bearer %s", token))
            }
            chain.proceed(request.build())
        }
    }
}