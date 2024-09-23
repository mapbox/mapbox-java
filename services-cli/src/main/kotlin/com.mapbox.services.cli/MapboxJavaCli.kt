package com.mapbox.services.cli

import com.google.gson.GsonBuilder
import com.mapbox.services.cli.validator.DirectionsResponseValidator
import com.mapbox.services.cli.validator.ValidatorInput
import com.mapbox.services.cli.validator.ValidatorResult
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

    private const val COMMAND_HELP = "h"
    private const val COMMAND_FILE_INPUT = "f"
    private const val COMMAND_JSON_INPUT = "j"
    private const val COMMAND_STDIN_INPUT = "s"
    private const val COMMAND_PRETTY_PRINT = "p"
    private const val COMMAND_FAIL_ON_CONVERT_BACK = "c"

    @JvmStatic
    fun main(args: Array<String>) {
        val options = Options()
            .addOption(
                Option.builder(COMMAND_HELP)
                    .longOpt("help")
                    .desc("Shows this help message")
                    .build()
            )
            .addOption(
                Option.builder(COMMAND_FILE_INPUT)
                    .longOpt("file")
                    .hasArg(true)
                    .desc("Path to a json file or directory. If directory is provided " +
                            "all files in it will be processed.")
                    .required(false)
                    .build()
            )
            .addOption(
                Option.builder(COMMAND_JSON_INPUT)
                    .longOpt("json")
                    .hasArg(true)
                    .desc("String containing the json. Instead of providing files it " +
                            "is possible to relay on the json directly.")
                    .required(false)
                    .build()
            )
            .addOption(
                Option.builder(COMMAND_STDIN_INPUT)
                    .longOpt("stdin")
                    .hasArg(false)
                    .desc("Stream redirection to the executable instead of file or " +
                            "string.")
                    .required(false)
                    .build()
            )
            .addOption(
                Option.builder(COMMAND_PRETTY_PRINT)
                    .longOpt("pretty")
                    .desc("Pretty printing of results. Colored and indented JSON " +
                            "output. It consist of many characters which are not visible in the " +
                            "console - that why it might be hard to parse such JSON. If you want " +
                            "to parse the result it's recommended not use this option.")
                    .required(false)
                    .build()
            )
            .addOption(
                Option.builder(COMMAND_FAIL_ON_CONVERT_BACK)
                    .longOpt("convert-fail")
                    .desc("Exit also with code 1 if the conversion back to JSON fails. " +
                            "This is useful to check if mapbox-java produces the same JSON file " +
                            "that was provided as an input from its internal structures.")
                    .required(false)
                    .build()
            )

        try {
            val commandLine = DefaultParser().parse(options, args)
            parseCommands(commandLine, options)
        } catch (pe: ParseException) {
            println(pe.message)
            printHelp(options)
        }
    }

    private fun parseCommands(commandLine: CommandLine, options: Options) {
        if (commandLine.hasOption(COMMAND_HELP) || commandLine.options.isEmpty()) {
            printHelp(options)
            return
        }

        val directionsResponseValidator = DirectionsResponseValidator()
        val results = mutableListOf<ValidatorResult>()

        when {
            commandLine.hasOption(COMMAND_JSON_INPUT) -> {
                val jsonInput = commandLine.getOptionValue(COMMAND_JSON_INPUT)
                results.add(directionsResponseValidator.parseJson(jsonInput))
            }

            commandLine.hasOption(COMMAND_FILE_INPUT) -> {
                val fileInput = commandLine.getOptionValue(COMMAND_FILE_INPUT)
                results.addAll(directionsResponseValidator.parseFile(fileInput))
            }

            commandLine.hasOption(COMMAND_STDIN_INPUT) -> {
                results.add(directionsResponseValidator.parseStdin())
            }
        }

        val failure = !results.all { it.success } ||
                (commandLine.hasOption(COMMAND_FAIL_ON_CONVERT_BACK) && !results.all { it.convertsBack })

        printResult(results, failure, commandLine.hasOption(COMMAND_PRETTY_PRINT))

        if (failure) {
            System.exit(1)
        }
    }

    private fun printHelp(options: Options) {
        val syntax = "java -jar services-cli/build/libs/services-cli.jar <option>"
        val header = "\nMapbox Java CLI. Validates DirectionsApi responses." +
                "CHeck correctness of the provided JSON (files or strings). " +
                "Process with code 1 on parse errors or (optionally) on conversion " +
                "back. Exits with code 0 on success. Returns JSON formatted result."
        HelpFormatter().printHelp(syntax, header, options, "")
    }

    private fun printResult(
        results: List<ValidatorResult>, failure: Boolean, prettyPrint: Boolean
    ) {
        var message = GsonBuilder().also { if (prettyPrint) it.setPrettyPrinting() }
            .registerTypeAdapterFactory(ValidatorInput.gsonAdapter()).create().toJson(results)
        if (prettyPrint) {
            message = (if (failure) "\u001B[31m" else "\u001B[32m") + "$message\u001B[0m\n"
        }
        print(message)
    }
}
