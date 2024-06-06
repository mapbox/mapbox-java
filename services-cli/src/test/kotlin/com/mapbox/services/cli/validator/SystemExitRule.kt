package com.mapbox.services.cli.validator

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.security.Permission

/**
 * Use this rule to test what code was called to exit process.
 */
class SystemExitRule : TestWatcher() {

    class ExitException(val status: Int) : SecurityException("System.exit($status) called.")

    private val systemSecurityManager = System.getSecurityManager()
    private val testSecurityManager = object : SecurityManager() {

        override fun checkPermission(perm: Permission?) {
            // allow anything
        }

        override fun checkPermission(perm: Permission?, context: Any?) {
            // allow anything
        }

        override fun checkExit(status: Int) {
            throw ExitException(status)
        }
    }

    override fun starting(description: Description) {
        System.setSecurityManager(testSecurityManager)
    }

    override fun finished(description: Description) {
        System.setSecurityManager(systemSecurityManager)
    }
}
