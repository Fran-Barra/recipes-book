package com.recipesbook.vault

import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.widget.Button
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.recipesbook.R
import com.recipesbook.security.BiometricAuthViewModel

@Composable()
fun VaultComposable() {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<BiometricAuthViewModel, BiometricAuthViewModel.ProfileViewModelFactory> { factory ->
        factory.create(localContext)
    }
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    val biometricManager = remember { BiometricManager.from(localContext) }

    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
    }

    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometric features are available
            if (isAuthenticated) {
                Text(text = "This is the profile")
//                Button(onClick = onNavigateToFriends, Modifier.wrapContentHeight()) {
//                    Text(text = "Navigate to friends")
//                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.vault_page_not_build),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Text(text = "You need to authenticate")
            }
        }

        //TODO: move all this to another place
        //TODO: remove all text
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Text(text = "This phone is not prepared for biometric features")
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Text(text = "Biometric auth is unavailable")
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            Text(text = "You can't use biometric auth until you have updated your security details")
        }
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            Text(text = "You can't use biometric auth with this Android version")
        }
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
            Text(text = "You can't use biometric auth")
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Text(text = "You can't use biometric auth")
        }
    }
}