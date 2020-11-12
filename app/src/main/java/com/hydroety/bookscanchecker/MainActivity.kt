package com.hydroety.bookscanchecker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName
    private val ARG_OBJECT = "object"
    private val ISBN_LEN = 13
    private var lastText: String = ""

    companion object {
        const val REQUEST_CAMERA_PERMISSION:Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_white_apps_24)

        checkPermissions()
        initQRCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CAMERA_PERMISSION -> { initQRCamera() }
        }
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

    private fun showDetail(isbn: String) {
        val startActivity = Intent()
        startActivity.putExtra(ARG_OBJECT, isbn)
        startActivity.setClass(this, DetailActivity::class.java)
        startActivity(startActivity)
    }

    private fun checkPermissions() {
        // already we got permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume()
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 999)
        }
    }

    @SuppressLint("WrongConstant")
    private fun initQRCamera() {
        val isReadPermissionGranted = (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        val isWritePermissionGranted = (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        val isCameraPermissionGranted = (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

        if (isReadPermissionGranted && isWritePermissionGranted && isCameraPermissionGranted) {
            openQRCamera()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun openQRCamera() {
        barcodeView.decodeContinuous(object : BarcodeCallback {
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

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) { }
        })
    }
}