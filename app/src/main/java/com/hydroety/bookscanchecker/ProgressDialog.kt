package com.hydroety.bookscanchecker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ProgressDialog : DialogFragment() {

    companion object {
        fun newInstance(message: String): ProgressDialog {
            val instance = ProgressDialog()
            val arguments = Bundle()
            arguments.putString("message", message)
            instance.arguments = arguments
            return instance
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString("message")
        return activity?.let {
            val inflater = it.layoutInflater
            val view = inflater.inflate(R.layout.dialog_progress, null)
            val messageTextView = view.findViewById(R.id.progress_message) as TextView
            messageTextView.text = message

            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                    .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class SimpleDialogFragment(val title: String, val message: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.general_ok),
                            DialogInterface.OnClickListener { _, _ ->
                                // User clicked OK button
                            })
                    .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
