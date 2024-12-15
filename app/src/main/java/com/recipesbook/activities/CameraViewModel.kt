package com.recipesbook.activities

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _isCaptured = MutableStateFlow<Boolean>(false)
    val isCaptured = _isCaptured.asStateFlow()

    private var imageCaptureHandler: ImageCaptureHandler? = null

    fun initialize(activityResultRegistry: ActivityResultRegistry) {
        val handleCaptured = { _isCaptured.value = true}

        imageCaptureHandler = ImageCaptureHandler(
            context.contentResolver,
            activityResultRegistry,
            handleCaptured
        )
    }

    fun captureImage() {
        imageCaptureHandler?.requestImageCapture { uri : Uri? ->
            _imageUri.value = uri
        }
    }
}

class ImageCaptureHandler(
    private val contentResolver: ContentResolver,
    private val activityResultRegistry: ActivityResultRegistry,
    private val wasCaptured : () -> Unit
) {
    private var capturedImageUri: Uri? = null
    private var onImageCaptured: ((Uri?) -> Unit)? = null

    private val cameraLauncher = activityResultRegistry.register(
        "cameraLauncher",
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onImageCaptured?.invoke(capturedImageUri)
            wasCaptured.invoke()
        } else {
            onImageCaptured?.invoke(null)
        }
    }

    private val permissionLauncher = activityResultRegistry.register(
        "permissionLauncher",
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        if (cameraGranted) {
            capturedImageUri = createImageUri()
            capturedImageUri?.let { cameraLauncher.launch(it) }
        } else {
            onImageCaptured?.invoke(null) // Permissions denied
        }
    }

    fun requestImageCapture(callback: (Uri?) -> Unit) {
        onImageCaptured = callback
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun createImageUri(): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
}

