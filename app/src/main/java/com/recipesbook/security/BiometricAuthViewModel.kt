package com.recipesbook.security

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.recipesbook.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = BiometricAuthViewModel.ProfileViewModelFactory::class)
class BiometricAuthViewModel  @AssistedInject constructor(
    @Assisted context: Context,
    biometricAuthManager: BiometricAuth,
) : ViewModel()  {

    private var _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        biometricAuthManager.authenticate(
            context,
            onError = {
                _isAuthenticated.value = false
                Toast.makeText(context, context.getString(R.string.biometric_error_in_auth), Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                _isAuthenticated.value = true
            },
            onFail = {
                _isAuthenticated.value = false
                Toast.makeText(context, context.getString(R.string.biometric_failed_auth), Toast.LENGTH_SHORT).show()
            }
        )
    }

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(context: Context): BiometricAuthViewModel
    }
}