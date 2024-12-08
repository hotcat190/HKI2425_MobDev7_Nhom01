package com.example.androidcookbook.ui.features.aigen

import android.net.Uri
import com.example.androidcookbook.domain.model.ingredient.Ingredient
import com.google.gson.annotations.Expose
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


data class AIGenUiState(
    @Expose val mealTitle: String = "",
    @Expose val portion: String = "",
    @Expose val cookingTime: String = "",
    @Expose val ingredients: MutableList<Ingredient> = mutableListOf(),
    val response: String = "",
    @Expose val servedAs: String = "Main Dish",
    @Expose val timeMeasurement: String = "Minute",
    @Expose val note: String = "",
    val recipes: MutableList<String> = mutableListOf(),
    val isTakingInput: Boolean = true,
    val isProcessing: Boolean = false,
    val isDoneUploadingImage:Boolean = false,
    val isDone: Boolean = false,
)