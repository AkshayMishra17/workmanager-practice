package com.example.workmanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager.ui.theme.WorkManagerTheme

class MainActivity : ComponentActivity() {
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private val STORAGE_PERMISSION_CODE = 1000

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the ActivityResultLauncher
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        // Handle the selected image URI here
                         enqueueImageUpload(uri)
                    }
                }
            }
        setContent {
            WorkManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Screen(requestStoragePermission = { requestStoragePermission() })
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        } else {
            selectImage()
        }
    }
}

private fun enqueueImageUpload(imageUri: Uri){
    val inputData = Data.Builder()
        .putString("IMAGE_URI",imageUri.toString()).build()

    //one time request
    val uploadWorkRequest = OneTimeWorkRequestBuilder<ImageUploadWorker>()
        .setInputData(inputData).build()

    WorkManager.getInstance().enqueue(uploadWorkRequest)
}