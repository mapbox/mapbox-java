package com.mapbox.services.cli.validator

import com.google.common.truth.Truth.assertThat
import com.mapbox.services.cli.MapboxJavaCli
import org.junit.Rule
import org.junit.Test


class MapboxJavaCliTest {

    @Rule
    @JvmField
    val systemOutRule = SystemOutRule()

    @Test
    fun `should display successful results as json`() {
        val testFile = "./src/test/resources/directions_v5.json"

        MapboxJavaCli.main(arrayOf("-f", testFile))

        val expected = """[{"filename":"directions_v5.json","success":true,"converts_back":false}]"""
        assertThat(systemOutRule.results()).isEqualTo(expected)
    }

    @Test
    fun `should not crash with missing arguments`() {
        MapboxJavaCli.main(arrayOf())

        val consoleOutput = systemOutRule.results()
        assertThat(consoleOutput).contains("Missing required option")
    }

    @Test
    fun `should display help with other arguments`() {
        val testFile = "./src/test/resources/directions_v5.json"
        MapboxJavaCli.main(arrayOf("-h", "-f", testFile))

        val consoleOutput = systemOutRule.results()

        assertThat(consoleOutput).contains("Shows this help message")
    }
}
