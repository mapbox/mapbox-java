package com.mapbox.services.cli

import com.google.gson.Gson
import com.mapbox.services.cli.validator.DirectionsResponseValidator
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

/**
 * Entry point for the command line interface.
 */
object MapboxJavaCli {

    private const val COMMAND_FILE_INPUT = "f"
    private const val COMMAND_HELP = "h"

    @JvmStatic
    fun main(args: Array<String>) {
        val options = Options()
            .addOption(Option.builder(COMMAND_HELP)
                .longOpt("help")
                .desc("Shows this help message")
                .build())
            .addOption(Option.builder(COMMAND_FILE_INPUT)
                .longOpt("file")
                .hasArg(true)
                .desc("Path to a json file or directory")
                .required()
                .build())

        try {
            val commandLine = DefaultParser().parse(options, args)
            parseCommands(commandLine, options)
        } catch (pe: ParseException) {
            println(pe.message)
            printHelp(options)
        }
    }

    private fun parseCommands(commandLine: CommandLine, options: Options) {
        if (commandLine.hasOption(COMMAND_HELP)) {
            printHelp(options)
        }

        val fileInput = commandLine.getOptionValue(COMMAND_FILE_INPUT)
        val directionsResponseValidator = DirectionsResponseValidator()
        val results = directionsResponseValidator.parse(fileInput)
        print(Gson().toJson(results))
    }

    private fun printHelp(options: Options) {
        val syntax = "java -jar services-cli/build/libs/services-cli-all.jar"
        HelpFormatter().printHelp(syntax, options)
    }
}
