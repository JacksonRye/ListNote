package com.computerwizards.listnote

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File


private const val REQUEST_ABOUT = 0
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_SHOW_IMAGE = 2
private const val DIALOG_IMAGE = "dialog_image"
private const val DIALOG_ABOUT = "dialog_about"

class SettingsFragment : Fragment() {

    private lateinit var profileImage: CircleImageView
    private lateinit var aboutButton: Button
    private lateinit var cameraButton: ImageButton
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        aboutButton = view.findViewById(R.id.about_button)
        profileImage = view.findViewById(R.id.profile_image)
        cameraButton = view.findViewById(R.id.camera_button)


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteRepository = NoteRepository.get()

        photoFile = noteRepository.getPhotoFile()
        photoUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.computerwizards.listnote.fileprovider",
            photoFile
        )
        updateUI()

    }

    private fun updateUI() {
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            profileImage.setImageBitmap(bitmap)
        } else {
            profileImage.setImageDrawable(null)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            profileImage.setImageBitmap(imageBitmap)
            updatePhotoView()
        }
    }

    override fun onStart() {
        super.onStart()

        profileImage.apply {
            setOnClickListener {

                ShowImageDialogFragment.newInstance(photoFile).apply {
                    setTargetFragment(this@SettingsFragment, REQUEST_SHOW_IMAGE)
                    show(this@SettingsFragment.parentFragmentManager, DIALOG_IMAGE)
                }

            }
        }

        cameraButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(
                    captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(
                        captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )

                    startActivityForResult(captureImage, REQUEST_IMAGE_CAPTURE)
                }
            }

        }

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