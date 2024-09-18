package com.recipesbook.apiManagement

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.recipesbook.DetailedRecipeModel
import com.recipesbook.R
import com.recipesbook.RecipeModel
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject


class RecipeBookApiImpl @Inject constructor() {
    fun getRandom(callbacks: RequestResponseCallbacks<RecipeModel>) {
        val call: Call<RecipeModel> = createService(callbacks.context).getRandom()
        manageCall(call, callbacks, R.string.fail_recipe_retrieve)
    }

    fun getDetailedModel(id: Int, callbacks: RequestResponseCallbacks<DetailedRecipeModel>) {
        val call: Call<DetailedRecipeModel> = createService(callbacks.context).getDetailedModel(id)
        manageCall(call, callbacks, R.string.fail_recipe_retrieve)
    }

    private fun createService(context : Context) : RecipesBookApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.recipes_api))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RecipesBookApi::class.java)
    }

    private fun <T> manageCall(
        call : Call<T>,
        callbacks : RequestResponseCallbacks<T>,
        @StringRes toastMessage : Int? = null
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(response: Response<T>?, retrofit: Retrofit?) {
                callbacks.loadingFinished()
                if (response?.isSuccess == true) {
                    val jokes = response.body()
                    callbacks.onSuccess(jokes)
                } else {
                    val content: String = R.string.fail_retrieving_data.toString()
                    onFailure(Exception(content))
                }
            }

            override fun onFailure(t: Throwable?) {
                val message = toastMessage ?: R.string.something_went_wrong
                Toast.makeText(callbacks.context, message, Toast.LENGTH_SHORT).show()

                callbacks.onFail()
                callbacks.loadingFinished()
            }
        })
    }
}