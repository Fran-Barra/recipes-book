package com.recipesbook.apiManagement

import android.content.Context

data class RequestResponseCallbacks<T>(
    val context: Context,
    val onSuccess: (T) -> Unit,
    val onFail: ()->Unit,
    val loadingFinished: ()->Unit
)