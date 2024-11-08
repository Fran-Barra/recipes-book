package com.recipesbook.vault


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
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.vault_page_not_build),
            modifier = Modifier.align(Alignment.Center)
        )
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