package com.mapbox.services.cli.validator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertTrue

class DirectionsResponseValidatorTest {

    private val directionsResponseValidator = DirectionsResponseValidator()

    @Test
    fun `should successfully read file with DirectionsResponse json`() {
        val testFile = "./src/test/resources/directions_v5.json"

        val results = directionsResponseValidator.parse(testFile)

        assertTrue(results[0].success)
        assertThat(results[0].filename).isEqualTo("directions_v5.json")
        assertThat(results[0].throwable).isNull()
    }

    @Test
    fun `should detect if file is not DirectionsResponse json`() {
        val testFile = "./src/test/resources/geojson_feature.json"

        val results = directionsResponseValidator.parse(testFile)

        assertThat(results[0].success).isFalse()
        assertThat(results[0].filename).isEqualTo("geojson_feature.json")
        assertThat(results[0].throwable).isNotNull()
        assertThat(results[0].convertsBack).isFalse()
    }

    @Test(expected = Exception::class)
    fun `should crash when json does not exist`() {
        val testFile = "not a real file path"

        val results = directionsResponseValidator.parse(testFile)

        assertTrue(results[0].success)
    }

    @Test
    fun `should parse every file in the directory`() {
        val testFile = "./src/test/resources"

        val results = directionsResponseValidator.parse(testFile)

        assertThat(results.size).isGreaterThan(1)
    }
}
