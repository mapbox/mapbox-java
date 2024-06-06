package com.mapbox.services.cli.validator

import com.google.common.truth.Truth.assertThat
import com.mapbox.services.cli.MapboxJavaCli
import org.junit.Rule
import org.junit.Test
import java.io.File


class MapboxJavaCliTest {

    @Rule
    @JvmField
    val systemOutRule = SystemOutRule()

    @Rule
    @JvmField
    val systemExit = SystemExitRule()

    @Test
    fun `should display successful results as json from file`() {
        val testFile = "./src/test/resources/directions_v5.json"

        MapboxJavaCli.main(arrayOf("-f", testFile))

        val expected =
            """[{"input":{"type":"File","name":"directions_v5.json"},"success":true,"converts_back":false}]"""
        assertThat(systemOutRule.results()).isEqualTo(expected)
    }

    @Test
    fun `should display successful results as json from string`() {
        val json = File("./src/test/resources/directions_v5.json").readText(Charsets.UTF_8)

        MapboxJavaCli.main(arrayOf("-j", json))

        val expected = """[{"input":{"type":"Json"},"success":true,"converts_back":false}]"""
        assertThat(systemOutRule.results()).isEqualTo(expected)
    }

    @Test
    fun `should show help with missing arguments`() {
        MapboxJavaCli.main(arrayOf())

        val consoleOutput = systemOutRule.results()
        assertThat(consoleOutput).contains("Shows this help message")
    }

    @Test
    fun `should display help with other arguments`() {
        val testFile = "./src/test/resources/directions_v5.json"
        MapboxJavaCli.main(arrayOf("-h", "-f", testFile))

        val consoleOutput = systemOutRule.results()

        assertThat(consoleOutput).contains("Shows this help message")
    }

    @Test
    fun `should make output pretty green`() {
        val testFile = "./src/test/resources/directions_v5.json"
        MapboxJavaCli.main(arrayOf("-p", "-f", testFile))

        val consoleOutput = systemOutRule.results()

        val expectedPrettyPrefix = "\u001B[32m"
        val expectedPrettySuffix = "\u001B[0m\n"
        assertThat(consoleOutput).contains(expectedPrettyPrefix)
        assertThat(consoleOutput).contains(expectedPrettySuffix)
    }

    @Test
    fun `should make output pretty red`() {
        val json = "not a json string"
        try {
            MapboxJavaCli.main(arrayOf("-p", "-j", json))
        } catch (exitException: SystemExitRule.ExitException) {
            assertThat(exitException.status).isEqualTo(1)
        }

        val consoleOutput = systemOutRule.results()

        val expectedPrettyPrefix = "\u001B[31m"
        val expectedPrettySuffix = "\u001B[0m\n"
        assertThat(consoleOutput).contains(expectedPrettyPrefix)
        assertThat(consoleOutput).contains(expectedPrettySuffix)
    }

    @Test
    fun `exits with code 1 when back conversion failed`() {
        val testFile = "./src/test/resources/directions_v5.json"
        var expectedCode: Int? = null
        try {
            MapboxJavaCli.main(arrayOf("-c", "-f", testFile))
        } catch (exitException: SystemExitRule.ExitException) {
            expectedCode = exitException.status
        }
        assertThat(expectedCode).isEqualTo(1)
    }
}
