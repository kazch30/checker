package com.hydroety.bookscanchecker

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.simpleName
    private val ARG_OBJECT = "object"
    private lateinit var bookApi:BooksApi
    private var isbn:String = ""
    private var filePath:String = ""

    companion object {
        private lateinit var compositeDisposable: CompositeDisposable

        fun getCompositeDisposable() : CompositeDisposable {
            return this.compositeDisposable
        }

        fun setCompositeDisposable(compositeDisposable: CompositeDisposable) {
            this.compositeDisposable = compositeDisposable
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreate()->")
        setContentView(R.layout.activity_detail)

        val intent = getIntent();
        isbn = intent.getStringExtra(ARG_OBJECT) ?: ""
        if (BuildConfig.DEBUG) Log.d(TAG, "isbn : $isbn")
        bookApi = BooksApi()
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onCreate()")
    }

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) Log.i(TAG, "onStart()->")
        setCompositeDisposable(CompositeDisposable())
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onStart()")
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) Log.i(TAG, "onResume()->")
        if (isbn.isNotEmpty()) searchBooks()
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onResume()")
    }

    override fun onStop() {
        super.onStop()
        if (BuildConfig.DEBUG) Log.i(TAG, "onStop()->")
        getCompositeDisposable().dispose()
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onStop()")
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) Log.i(TAG, "onDestroy()->")
        removeCacheFile()
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onDestroy()")
        super.onDestroy()
    }

    private fun searchBooks() {
        getCompositeDisposable().add(
            bookApi.getBookInfo(isbn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (BuildConfig.DEBUG) Log.d(TAG, "doOnSuccess() success tread id= " + Thread.currentThread().name)
                    when (it) {
                        is BooksApiError.success -> {
                            showDetailInfo()
                        }
                        is BooksApiError.apiError -> {
                            if (BuildConfig.DEBUG) Log.e(TAG, "apiError")
                        }
                        is BooksApiError.decodeError -> {
                            if (BuildConfig.DEBUG) Log.e(TAG, "decodeError")
                        }
                        else -> {
                            if (BuildConfig.DEBUG) Log.e(TAG, "other error")
                        }
                    }

                }
                .doOnError {
                    if (BuildConfig.DEBUG) Log.e(TAG, "doOnError tread id= " + Thread.currentThread().name)
                    if (BuildConfig.DEBUG) Log.e(TAG, "Exception = ", it)
                }
                .subscribe({}, {})
        )

    }

    private fun showDetailInfo() {
        val authors = mutableListOf<String>()
        bookApi.getBooksInfo()?.let {
            if (it.totalItems > 0) {
                val volume = it.items[0]
                val info = volume.volumeInfo
                bookTitle.text = info.title
                if (info.authors.size > 0 ) {
                    bookAuthors.text = info.authors[0]
                    detailAuthors.text = info.authors[0]

                    info.authors.forEach { authors.add(it) }
                }
                val cnt = info.pageCount.toInt() / 100 // TODO
                booksCountStart.text =  getString(R.string.detail_msgstart)
                booksCountEnd.text = getString(R.string.detail_msgend)
                booksCount.text = cnt.toString()
                detailPublisher.text = "幻冬舎" // TODO
                detailAdress.text = "東京都渋谷区千駄ヶ谷４−９−７ー２F" // TODO
                detailTelnumber.text = "03-5411-6222" // TODO

                if (BuildConfig.DEBUG) Log.d(TAG, "thumbnail : " + info.imageLinks.thumbnail)
                downloadThumbnail(info.imageLinks.thumbnail)

            }

        }
    }

    private fun downloadThumbnail(url: String) {
        if (BuildConfig.DEBUG) Log.i(TAG, "downloadThumbnail()->")


        getCompositeDisposable().add(
            Single.create<Boolean> { e ->
                if (BuildConfig.DEBUG) Log.i(TAG, "Glide.with()->")
                if (BuildConfig.DEBUG) Log.d(TAG, "tread id= " + Thread.currentThread().name)
                val bitmap = Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()

                val directory = ContextWrapper(this).getDir(
                    getString(R.string.dir_name),
                    Context.MODE_PRIVATE
                )
                val file = File(directory, getString(R.string.thumbnail_name))
                FileOutputStream(file).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
                if (BuildConfig.DEBUG) Log.i(TAG, "<-Glide.with()")

                e.onSuccess(true)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (BuildConfig.DEBUG) Log.d(TAG, "doOnSuccess() Glide.with success tread id= " + Thread.currentThread().name)
                    setThumbnail()
                }
                .doOnError {
                    if (BuildConfig.DEBUG) Log.e(TAG, "doOnError = ", it)
                }
                .subscribe({}, {})
        )

        if (BuildConfig.DEBUG) Log.i(TAG, "<-downloadThumbnail()")
    }

    private fun setThumbnail() {
        if (BuildConfig.DEBUG) Log.i(TAG, "setThumbnail()->")
        val cacheFile = cacheDir.path.toString()
        filePath = cacheFile.substring(0, cacheFile.lastIndexOf('/')) + getString(R.string.storage_name) + getString(R.string.thumbnail_name)
        if (BuildConfig.DEBUG) Log.d(TAG, "file=" + filePath)
        if (File(filePath).exists()) {
            val fin = FileInputStream(filePath)
            val bmp = BitmapFactory.decodeStream(fin)
            thumbnailView.setImageBitmap(bmp)
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "<-setThumbnail()")
    }

    private fun removeCacheFile() {
        if (filePath.isNotEmpty()) {
            if (File(filePath).exists()) File(filePath).delete()
        }
    }
}

