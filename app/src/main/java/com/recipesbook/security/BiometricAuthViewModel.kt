package com.recipesbook.security

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//TODO: remove all the text from here
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
                Toast.makeText(context, "There was an error in the authentication", Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                _isAuthenticated.value = true
            },
            onFail = {
                _isAuthenticated.value = false
                Toast.makeText(context, "The authentication failed, try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    @AssistedFactory
    interface ProfileViewModelFactory {
        fun create(context: Context): BiometricAuthViewModel
    }
}