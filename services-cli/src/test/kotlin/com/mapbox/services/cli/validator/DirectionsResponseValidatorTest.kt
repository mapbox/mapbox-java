package com.mapbox.services.cli.validator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class DirectionsResponseValidatorTest {

    private val directionsResponseValidator = DirectionsResponseValidator()

    @Test
    fun `should successfully read file with DirectionsResponse json`() {
        val testFile = "./src/test/resources/directions_v5.json"

        val results = directionsResponseValidator.parseFile(testFile)

        assertTrue(results[0].success)
        assertThat(results[0].filename).isEqualTo("directions_v5.json")
        assertThat(results[0].error).isNull()
    }

    @Test
    fun `should successfully read string with correct DirectionsResponse json`() {
        val testText = File("./src/test/resources/directions_v5.json").readText(Charsets.UTF_8)

        val result = directionsResponseValidator.parseJson(testText)

        assertTrue(result.success)
        assertThat(result.input).isEqualTo(ValidatorInput.Json)
        assertThat(result.error).isNull()
    }

    @Test
    fun `should detect if file is not DirectionsResponse json`() {
        val testFile = "./src/test/resources/geojson_feature.json"

        val results = directionsResponseValidator.parseFile(testFile)

        assertThat(results[0].success).isFalse()
        assertThat(results[0].filename).isEqualTo("geojson_feature.json")
        assertThat(results[0].error).isNotNull()
        assertThat(results[0].convertsBack).isFalse()
    }

    @Test
    fun `should detect if provided string is not DirectionsResponse formatted json`() {
        val testText = File("./src/test/resources/geojson_feature.json")
            .readText(Charsets.UTF_8)


        val result = directionsResponseValidator.parseJson(testText)

        assertThat(result.success).isFalse()
        assertThat(result.input).isEqualTo(ValidatorInput.Json)
        assertThat(result.error).isNotNull()
        assertThat(result.convertsBack).isFalse()
    }

    @Test(expected = Exception::class)
    fun `should crash when json file does not exist`() {
        val testFile = "not a real file path"

        val results = directionsResponseValidator.parseFile(testFile)

        assertTrue(results[0].success)
    }

    @Test
    fun `should parse every file in the directory`() {
        val testFile = "./src/test/resources"

        val results = directionsResponseValidator.parseFile(testFile)

        assertThat(results.size).isGreaterThan(1)
    }

    private val ValidatorResult.filename: String? get() = (input as? ValidatorInput.File)?.name
}
