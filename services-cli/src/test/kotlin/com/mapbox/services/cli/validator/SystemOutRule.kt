package com.mapbox.services.cli.validator

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Use this rule to test what has been printed to the console.
 */
class SystemOutRule : TestWatcher() {

    private val systemOutStream = ByteArrayOutputStream()
    private lateinit var stdout: PrintStream

    /**
     * In the test, assert that these results are expected.
     */
    fun results(): String = systemOutStream.toString()

    override fun starting(description: Description) {
        stdout = System.out
        System.setOut(PrintStream(systemOutStream))
    }

    override fun finished(description: Description) {
        System.setOut(stdout)
    }
}
