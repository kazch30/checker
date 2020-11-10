package com.hydroety.bookscanchecker

import android.util.Log
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

sealed class BooksApiError: Throwable() {
//    object incorrect: BooksApiError()
    object apiError: BooksApiError()
    object decodeError: BooksApiError()
    object success: BooksApiError()
}

class BooksApi {
    private val TAG = BooksApi::class.simpleName
    private val REQ_PARAM = "isbn:"
    private var statusCode = 200
    private var errorMessage = ""
    private var booksinfo: BooksInfo?=null
    private var title:String=""
    private var authors = mutableListOf<String>()

    fun getBookInfo(isbn:String) : Single<BooksApiError> {

        return Single.create(SingleOnSubscribe<BooksApiError> { e ->

            val isbnParam = REQ_PARAM + isbn
            DetailActivity.getCompositeDisposable().add(
                RetrofitModule.apiClient.searchBooks(isbnParam)
                    .subscribeOn(Schedulers.io())
                    .doOnNext {
                        if (BuildConfig.DEBUG) Log.d(TAG, "tread id= " + Thread.currentThread().name)
                        if (BuildConfig.DEBUG) Log.d(TAG, "response=$it")
                        booksinfo = it

                        e.onSuccess(BooksApiError.success)
                    }
                    .doOnComplete {
                        if (BuildConfig.DEBUG) Log.d(TAG,"doOnComplete() tread id= " + Thread.currentThread().name)
                    }
                    .doOnError {
                        when(it) {
                            is HttpException -> {
                                if (BuildConfig.DEBUG) Log.e(TAG, "doOnError = ", it)
                                statusCode = it.code()
                                errorMessage = it.message ?: ""
                                if (BuildConfig.DEBUG) Log.e(TAG, "errorMessage = " + errorMessage)
                                if (BuildConfig.DEBUG) Log.e(TAG,"status code = " + statusCode)
//                                e.onError(Throwable(BooksApiError.apiError))
                                e.onSuccess(BooksApiError.apiError)
                            }
                            is IOException -> {
                                if (BuildConfig.DEBUG) Log.e(TAG, "IOException = ", it)
                                this.errorMessage = it.message ?: ""
                                if (BuildConfig.DEBUG) Log.e(TAG, "errorMessage = " + this.errorMessage)
//                                e.onError(Throwable(BooksApiError.apiError))
                                e.onSuccess(BooksApiError.apiError)
                            }
                            is JsonSyntaxException -> {
                                if (BuildConfig.DEBUG) Log.e(TAG, "JsonSyntaxException = ", it)
//                                e.onError(Throwable(BooksApiError.decodeError))
                                e.onSuccess(BooksApiError.decodeError)
                            }
                            else -> {
                                if (BuildConfig.DEBUG) Log.e(TAG, "Exception = ", it)
//                                e.onError(Throwable(BooksApiError.decodeError))
                                e.onSuccess(BooksApiError.decodeError)
                            }
                        }

                    }
                    .subscribe({}, {})
            )

        })

    }

    fun getBooksInfo() : BooksInfo? {
        return booksinfo
    }

    fun getDetailInfo(info:BooksInfo) {
        if (info.totalItems > 0 ) {
            val volume = info.items[0]
            if (volume.volumeInfo.authors.size > 0 ) {
                volume.volumeInfo.authors.forEach { authors.add(it) }
            }
        }


    }

    fun getStatusCode() : Int {
        return statusCode
    }

    fun getErrorMessage() : String {
        return errorMessage
    }

}