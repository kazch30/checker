package com.hydroety.bookscanchecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sample.*

class InputActivity : AppCompatActivity() {
    private val TAG = InputActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreate()->")
        setContentView(R.layout.activity_input)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Important step, we enable button on the left side of the toolbar
        toolbar.navigationIcon = getDrawable(R.drawable.ic_white_photo_camera_24) // Here we change default navigation button icon


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val collectionFragment = CollectionFragment()

        fragmentTransaction.add(R.id.container, collectionFragment)
        fragmentTransaction.commit()

        if (BuildConfig.DEBUG) Log.i(TAG, "<-onCreate()")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val startActivity = Intent()

        when (item.itemId) {
            android.R.id.home -> {
                startActivity.setClass(this, MainActivity::class.java)
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
        if (BuildConfig.DEBUG) Log.i(TAG, "onResume()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onResume()")
    }

    override fun onDestroy() {
        if (BuildConfig.DEBUG) Log.i(TAG, "onDestroy()->")
        if (BuildConfig.DEBUG) Log.i(TAG, "<-onDestroy()")
        super.onDestroy()
    }
}