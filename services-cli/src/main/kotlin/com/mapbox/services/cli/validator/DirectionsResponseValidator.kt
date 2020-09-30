package com.mapbox.services.cli.validator

import com.mapbox.api.directions.v5.models.DirectionsResponse
import java.io.File
import kotlin.text.Charsets.UTF_8

/**
 * Validate DirectionsResponse json strings.
 */
class DirectionsResponseValidator {

    /**
     * @param filePath path to the json file or directory
     * @return results for all the files
     */
    fun parse(filePath: String): List<ValidatorResult> {
        val inputFile = File(filePath)

        val results = mutableListOf<ValidatorResult>()
        inputFile.forEachFile { file ->
            val result = validateJson(file)
            results.add(result)
        }
        return results
    }

    private fun File.forEachFile(function: (File) -> Unit) = walk()
        .filter { !it.isDirectory }
        .forEach(function)

    private fun validateJson(file: File): ValidatorResult {
        val json = file.readText(UTF_8)
        return try {
            val directionsResponse = DirectionsResponse.fromJson(json)
            val toJson = directionsResponse.toJson()
            val convertsBack = json == toJson
            ValidatorResult(
                filename = file.name,
                success = true,
                convertsBack = convertsBack
            )
        } catch (throwable: Throwable) {
            ValidatorResult(
                filename = file.name,
                success = false,
                convertsBack = false,
                throwable = throwable
            )
        }
    }
}
