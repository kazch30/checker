package com.hydroety.bookscanchecker

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment : Fragment() {
    private val TAG = InputFragment::class.simpleName
    private val ARG_OBJECT = "object"
    private val isbnDigit = arrayOf(13, 10)
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

            textView.setText(HtmlCompat.fromHtml(getString(R.string.isbn_hint, isbnDigit[tag]), FROM_HTML_MODE_COMPACT))
            if (tag == 0) {
                editTextNumber.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                editTextNumber.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                defText.visibility = View.GONE
            }
            editTextNumber.setFocusableInTouchMode(true)
            editTextNumber.setFocusable(true)
            editTextNumber.requestFocus()

            button.setOnClickListener {
                var text = editTextNumber.text.toString()
                if (tag == 0) {
                    text = getString(R.string.isbn_def) + text
                }
                if (BuildConfig.DEBUG) Log.d(TAG, "setOnClickListener() text:" + text)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editTextNumber.requestFocus()
    }
}