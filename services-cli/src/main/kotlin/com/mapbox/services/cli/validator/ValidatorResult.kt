package com.mapbox.services.cli.validator

import com.google.gson.annotations.SerializedName
import com.mapbox.geojson.internal.typeadapters.RuntimeTypeAdapterFactory

data class ValidatorResult(
    val input: ValidatorInput,
    val success: Boolean,
    @SerializedName("converts_back")
    val convertsBack: Boolean,
    val error: String? = null
)


sealed class ValidatorInput {
    data class File(val name: String) : ValidatorInput()
    object Json : ValidatorInput()

    companion object {
        fun gsonAdapter(): RuntimeTypeAdapterFactory<ValidatorInput> {
            return RuntimeTypeAdapterFactory
                .of<ValidatorInput>(ValidatorInput::class.java, "type")
                .registerSubtype(
                    File::class.java,
                    File::class.simpleName
                )
                .registerSubtype(
                    Json::class.java,
                    Json::class.simpleName
                )
        }
    }
}


