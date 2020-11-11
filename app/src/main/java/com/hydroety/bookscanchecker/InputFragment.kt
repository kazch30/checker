package com.hydroety.bookscanchecker

import android.content.Intent
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
    private val ISBN_LEN = 10


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (BuildConfig.DEBUG) Log.i(TAG, "onCreateView()->")

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
                if (text.length == ISBN_LEN) {
                    if (tag == 0) {
                        text = getString(R.string.isbn_def) + text
                    }
                    if (BuildConfig.DEBUG) Log.d(TAG, "setOnClickListener() text:" + text)
                    showDetail(text)
                } else {
                    val dialog = SimpleDialogFragment(getString(R.string.invalid_isbn_title), getString(R.string.invalid_isbn_msg))
                    fragmentManager.run{
                        this?.let { it1 -> dialog.show(it1,"simple") }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editTextNumber.requestFocus()
    }

    fun showDetail(isbn: String) {
        context?.let {
            val startActivity = Intent()
            startActivity.putExtra(ARG_OBJECT, isbn)
            startActivity.setClass(it, DetailActivity::class.java)
            startActivity(startActivity)
        }
    }
}