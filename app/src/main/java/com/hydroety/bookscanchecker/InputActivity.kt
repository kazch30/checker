package com.hydroety.bookscanchecker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {
    private val TAG = InputActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreate()->")
        setContentView(R.layout.activity_input)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val collectionFragment = CollectionFragment()

        fragmentTransaction.add(R.id.container, collectionFragment)
        fragmentTransaction.commit()

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