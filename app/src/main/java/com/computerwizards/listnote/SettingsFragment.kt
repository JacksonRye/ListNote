package com.computerwizards.listnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

private const val REQUEST_ABOUT = 0
private const val DIALOG_ABOUT = "dialog_about"

class SettingsFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var aboutButton: Button
    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        aboutButton = view.findViewById(R.id.about_button)



        return view

    }

    override fun onStart() {
        super.onStart()

        aboutButton.setOnClickListener {
            AboutDialog.newInstance().apply {
                setTargetFragment(this@SettingsFragment, REQUEST_ABOUT)
                show(this@SettingsFragment.parentFragmentManager, DIALOG_ABOUT)
            }
        }
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}