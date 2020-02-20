package com.computerwizards.listnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class AboutDialog : DialogFragment() {

    private lateinit var okButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_about, container, false)

        okButton = view.findViewById(R.id.button_close)

        return view
    }

    override fun onStart() {
        super.onStart()
        okButton.setOnClickListener {
            this.dismiss()
        }
    }

    companion object {
        fun newInstance(): AboutDialog {
            return AboutDialog()
        }
    }


}