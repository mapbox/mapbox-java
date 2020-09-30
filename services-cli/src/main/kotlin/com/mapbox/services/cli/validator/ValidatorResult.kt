package com.mapbox.services.cli.validator

import com.google.gson.annotations.SerializedName

data class ValidatorResult(
    val filename: String,
    val success: Boolean,
    @SerializedName("converts_back")
    val convertsBack: Boolean,
    val throwable: Throwable? = null
)
