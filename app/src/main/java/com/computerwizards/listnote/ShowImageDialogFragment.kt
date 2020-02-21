package com.computerwizards.listnote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File


private const val ARG_IMAGE_PATH = "image_file"

class ShowImageDialogFragment : DialogFragment() {

    private lateinit var photoBitmap: Bitmap
    private lateinit var dialogImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoFile: File = arguments?.getSerializable(ARG_IMAGE_PATH) as File

        if (photoFile.exists()) {

            photoBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_show_image_dialog,
            container, false
        )

        dialogImage = view.findViewById(R.id.dialog_image)

        dialogImage.setImageBitmap(photoBitmap)

        return view
    }

    companion object {
        fun newInstance(photoFile: File): ShowImageDialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_IMAGE_PATH, photoFile)
            }
            return ShowImageDialogFragment().apply {
                arguments = args
            }
        }
    }

}