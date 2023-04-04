package search

import java.util.MissingFormatArgumentException
import kotlin.system.exitProcess

interface Messaging {
    val msg: String
    fun show(vararg strings: String) {
        try {
            println(this.msg.format(*strings)).also {
                if (this in Errors.values()) {
                    exitProcess(0)
                }
            }
        } catch(e: MissingFormatArgumentException) {
            println("$this: Invalid number of arguments.")
            exitProcess(0)
        }
    }
}

enum class Console(override val msg: String): Messaging {
    ListSize("Enter the number of people:"),
    List("Enter all people:"),
    Menu("=== Menu ===\n" +
            "1. Find a person\n" +
            "2. Print all people\n" +
            "0. Exit"
    ),
    MatchInput("Enter a name or email to search all matching people."),
    Print("=== List of people ==="),
    Found("%s person%s found:"),
    MatchingType("Select a matching strategy: ALL, ANY, NONE"),
    InvalidInput("Incorrect option! Try again.")

}

enum class Errors(override val msg: String): Messaging {
    Exit("Bye bye!")
}