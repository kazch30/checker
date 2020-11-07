package com.hydroety.bookscanchecker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    private val TAG = InfoActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreate()->")
        setContentView(R.layout.activity_info)

        if (BuildConfig.DEBUG) Log.i(TAG, "<-onCreate()")
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) Log.i(TAG, "onResume()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onResume()")
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) Log.i(TAG, "onDestroy()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onDestroy()")
        super.onDestroy()
    }

}