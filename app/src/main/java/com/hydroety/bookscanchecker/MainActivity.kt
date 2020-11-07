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
import kotlinx.android.synthetic.main.activity_sample.*


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
//    var barcodeView:DecoratedBarcodeView?=null
    private var lastText: String = ""

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result?.text == null || result.text == lastText) {
                return
            }
            if (BuildConfig.DEBUG) Log.d(TAG, "BarcodeCallback : " + result.text)
            lastText = result.text
            barcodeView?.setStatusText(result.text)




        }
        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Important step, we enable button on the left side of the toolbar
        toolbar.navigationIcon = getDrawable(R.drawable.ic_white_apps_24) // Here we change default navigation button icon


//        barcodeView = findViewById<DecoratedBarcodeView>(R.id.barcodeView)
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
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) Log.i(TAG, "onDestroy()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onDestroy()")
        super.onDestroy()
    }

}