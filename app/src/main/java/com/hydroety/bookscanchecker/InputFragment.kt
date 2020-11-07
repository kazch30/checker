package com.hydroety.bookscanchecker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class InputFragment : Fragment() {
    private val TAG = InputFragment::class.simpleName
    private val ARG_OBJECT = "object"
    private var tag:Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreateView()->")

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_input, container, false)

        if (BuildConfig.DEBUG) Log.i(TAG, "<-onCreateView()")
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            if (BuildConfig.DEBUG) Log.d(TAG, "ARG_OBJECT tag: " + getInt(ARG_OBJECT).toString())
            tag = getInt(ARG_OBJECT)
        }
    }
}