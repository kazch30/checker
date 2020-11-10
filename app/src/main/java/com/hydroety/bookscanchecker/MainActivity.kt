package com.hydroety.bookscanchecker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private val ARG_OBJECT = "object"
    private val ISBN_LEN = 13
    private var lastText: String = ""

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result?.text == null || result.text == lastText ||
                    result.text.length != ISBN_LEN || result.text.substring(0,3) != getString(R.string.isbn_def)) {
                if (BuildConfig.DEBUG) Log.d(TAG, "skip : " + result?.text)
                return
            }
            if (BuildConfig.DEBUG) Log.d(TAG, "BarcodeCallback : " + result.text)
            lastText = result.text
            showDetail(result.text)
        }
        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_white_apps_24)


        val formats = listOf(BarcodeFormat.EAN_13)
        barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView?.initializeFromIntent(intent)
        barcodeView?.decodeContinuous(callback)
        barcodeView?.barcodeView?.cameraSettings = CameraSettings()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val startActivity = Intent()

        when (item.itemId) {
            android.R.id.home -> {
                startActivity.setClass(this, InputActivity::class.java)
                startActivity(startActivity)
            }
            R.id.menu_info -> {
                startActivity.setClass(this, InfoActivity::class.java)
                startActivity(startActivity)
            }
            else -> {
                if (BuildConfig.DEBUG) Log.d(TAG, "onOptionsItemSelected() item not found.")
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        barcodeView?.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView?.pause()
        lastText = ""
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) Log.i(TAG, "onDestroy()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onDestroy()")
        super.onDestroy()
    }

    fun showDetail(isbn: String) {
        val startActivity = Intent()
        startActivity.putExtra(ARG_OBJECT, isbn)
        startActivity.setClass(this, DetailActivity::class.java)
        startActivity(startActivity)
    }

}