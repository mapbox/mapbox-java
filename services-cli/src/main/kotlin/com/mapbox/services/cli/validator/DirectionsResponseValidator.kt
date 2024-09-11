package com.mapbox.services.cli.validator

import com.mapbox.api.directions.v5.models.DirectionsResponse
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.stream.Collectors
import kotlin.text.Charsets.UTF_8

/**
 * Validate DirectionsResponse json strings.
 */
class DirectionsResponseValidator {

    /**
     * @param filePath path to the json file or directory
     * @return results for all the files
     */
    fun parseFile(filePath: String): List<ValidatorResult> {
        val inputFile = File(filePath)

        val results = mutableListOf<ValidatorResult>()
        inputFile.forEachFile { file ->
            val result = validateFile(file, ValidatorInput.File(file.name))
            results.add(result)
        }
        return results
    }

    /**
     * @param json JSON formatted string
     * @return results parsed from the JSON
     */
    fun parseJson(json: String): ValidatorResult = validateJson(json, ValidatorInput.Json)

    /**
     * Parses
     * @return results parsed from the JSON
     */
    fun parseStdin(): ValidatorResult {
        val json: String = BufferedReader(InputStreamReader(System.`in`))
            .lines().collect(Collectors.joining("\n"))
        return validateJson(json, ValidatorInput.Stdin)
    }

    private fun File.forEachFile(function: (File) -> Unit) = walk()
        .filter { !it.isDirectory }
        .forEach(function)

    private fun validateFile(file: File, input: ValidatorInput): ValidatorResult {
        val json = file.readText(UTF_8)
        return validateJson(json, input)
    }

    private fun validateJson(json: String, input: ValidatorInput): ValidatorResult {
        return try {
            val directionsResponse = DirectionsResponse.fromJson(json)
            val toJson = directionsResponse.toJson()
            val convertsBack = json == toJson
            ValidatorResult(
                input = input,
                success = true,
                convertsBack = convertsBack
            )
        } catch (throwable: Throwable) {
            ValidatorResult(
                input = input,
                success = false,
                convertsBack = false,
                error = throwable.message
            )
        }
    }

}
