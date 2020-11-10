package com.hydroety.bookscanchecker

import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiClient {

    @Headers("Accept: application/json")
    @GET("books/v1/volumes")
    fun searchBook(@Query("q") isbn: String): Observable<Response<ResponseBody>>

    @Headers("Accept: application/json")
    @GET("books/v1/volumes")
    fun searchBooks(@Query("q") isbn: String): Observable<BooksInfo>

}

object RetrofitModule {
    private const val ENDPOINT = "https://www.googleapis.com/"

    val apiClient: ApiClient
        get() = Retrofit.Builder()
            .client(getClient())
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiClient::class.java)

    private fun getClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient
                .Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()

        }
    }
}