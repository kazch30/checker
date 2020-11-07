package com.hydroety.bookscanchecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ScanView : Fragment() {
    private val TAG = ScanView::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreateView()->")

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_scan, container, false)

        val integrator: IntentIntegrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
        integrator.setPrompt("Scan a barcode")
        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()

        if (BuildConfig.DEBUG) Log.i(TAG, "<-onCreateView()")
        return fragmentView
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if(result.getContents() == null) {
                if (BuildConfig.DEBUG) Log.i(TAG, "onActivityResult() Cancelled.")

            } else {
                if (BuildConfig.DEBUG) Log.i(TAG, "onActivityResult() getContents : " + result.getContents())

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}