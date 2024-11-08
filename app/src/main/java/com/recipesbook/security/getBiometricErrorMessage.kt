package com.recipesbook.security

import android.content.Context
import androidx.biometric.BiometricManager
import com.recipesbook.R

fun canAuthenticate(context : Context, manager: BiometricManager) : AuthenticationAvailability =
    when (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> NotAvailable(context.getString(R.string.biometric_error_no_hardware))
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> NotAvailable(context.getString(R.string.biometric_error_hw_unavailable))
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> NotAvailable(context.getString(R.string.biometric_error_security_update_required))
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> NotAvailable(context.getString(R.string.biometric_error_unsupported))
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> NotAvailable(context.getString(R.string.biometric_status_unknown))
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> NotAvailable(context.getString(R.string.biometric_error_none_enrolled))
        else -> Available(context.getString(R.string.biometric_available))
    }

sealed interface AuthenticationAvailability
data class Available(val msg: String) : AuthenticationAvailability
data class NotAvailable(val errorMsg: String) : AuthenticationAvailability
