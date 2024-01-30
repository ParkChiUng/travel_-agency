package com.sessac.travel_agency.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Locale

class CommonHandler {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var pickMedia: ActivityResultLauncher<Intent>
    private lateinit var imageUri: Uri
    private lateinit var onImageSelected: ((Uri) -> Unit)
    private val dialogMap = HashMap<View, BottomSheetDialog>()

    fun spinnerHandler(items: Array<String>, binding: AutoCompleteTextView, context: Context) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        val autoCompleteTextView: AutoCompleteTextView = binding
        autoCompleteTextView.setAdapter(adapter)
    }

    fun imageCallback(activityResultRegistry: ActivityResultRegistry) {
        pickMedia = activityResultRegistry.register(
            "key",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                onImageSelected.invoke(imageUri)
            }
        }
    }

    fun imageSelect(onImageSelected: (Uri) -> Unit) {
        this.onImageSelected = onImageSelected
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickMedia.launch(galleryIntent)
    }

    fun dateHandler(firstDate: Array<String>, secondDate: AutoCompleteTextView, context: Context) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    }

    fun showDialog(view: View, context: Context) {
        dialog = BottomSheetDialog(context)

        // viewGroup 삭제
        if (view.parent != null) (view.parent as ViewGroup).removeView(view)

        dialog.setContentView(view)
        dialog.show()

        dialogMap[view] = dialog
    }

    fun dismissDialog(view: View) {
        dialogMap[view]?.dismiss()
    }
}