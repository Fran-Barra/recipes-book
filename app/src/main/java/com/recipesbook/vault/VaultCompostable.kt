package com.recipesbook.vault


import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.recipesbook.R
import com.recipesbook.activities.CameraHandler
import com.recipesbook.activities.createImageUri
import com.recipesbook.security.Available
import com.recipesbook.security.BiometricAuthViewModel
import com.recipesbook.security.NotAvailable
import com.recipesbook.security.canAuthenticate

@Composable()
fun VaultComposable() {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<BiometricAuthViewModel, BiometricAuthViewModel.ProfileViewModelFactory> { factory ->
        factory.create(localContext)
    }
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    val biometricManager = remember { BiometricManager.from(localContext) }
    val canAuthenticate = remember { canAuthenticate(localContext, biometricManager) }

    when (canAuthenticate) {
        is Available -> {
            if (isAuthenticated) VaultUnlockedComposable()
            else VaultLockedComposable()
        }
        is NotAvailable -> VaultNotWorkingComposable(errorMessage = canAuthenticate.errorMsg)
    }
}

@Composable
fun VaultUnlockedComposable() {
    // State to hold the captured image URI
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Handles the image capture
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Update the state with the captured image URI
            capturedImageUri?.let {
                Toast.makeText(context, "Image captured successfully!", Toast.LENGTH_SHORT).show()
                imageLoaded = true
            }
        } else {
            Toast.makeText(context, "Failed to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to create a content URI for saving the image
    fun createImageUri(): Uri? {
        val contentResolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[android.Manifest.permission.CAMERA] ?: false
        val storageGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        if (cameraGranted) {
            val uri = createImageUri()
            if (uri != null) {
                capturedImageUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Failed to create image file.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permissions not granted.", Toast.LENGTH_SHORT).show()
        }
    }



    // UI
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Display the captured image
            capturedImageUri?.let { uri ->
                Log.d("VaultUnlockedComposable", "uri: $uri")

                if (imageLoaded) {
                    val inputStream = context.contentResolver.openInputStream(capturedImageUri!!)
                    if (inputStream == null) {
                        Log.e("VaultUnlockedComposable", "Failed to open URI: $capturedImageUri")
                    } else {
                        Log.d("VaultUnlockedComposable", "Successfully opened URI")
                        inputStream.close()
                    }
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Captured Image",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to launch the camera
            Button(
                onClick = {permissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                }
            ) {
                Text("Set Picture")
            }
        }
    }
}

@Composable
fun VaultLockedComposable() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.requires_authentication),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun VaultNotWorkingComposable(errorMessage : String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}