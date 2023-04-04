package search

import java.io.File
import kotlin.system.exitProcess

class Search(private val args: Array<String>) {
    private val people = mutableListOf<String>()
    private val words = mutableMapOf<String, MutableList<Int>>()

    init {
        when(args.first()) {
            "--data" -> populate()
            else -> exitProcess(0)
        }

        menu@ while (true) {
            Console.Menu.show()
            when(readln().toIntOrNull()) {
                1 -> {
                    Console.MatchingType.show()
                    val input = readln().lowercase()
                    val strategies = listOf("any", "all", "none")
                    if (input in strategies) {
                        find(input)
                    }
                }
                2 -> peoplePrint()
                0 -> Errors.Exit.show()
                else -> Console.InvalidInput.show()
            }
        }
    }

    private fun populate() {
        File(args.last()).useLines { lines -> lines.forEach { line -> people.add(line) } }
        people.forEachIndexed { i, p ->
            p.split(" ").forEach { word ->
                if (words.containsKey(word.lowercase())) {
                    words.getValue(word.lowercase()).addAll(mutableListOf(i))
                } else {
                    words[word.lowercase()] = mutableListOf(i)
                }
            }
        }
    }

    private fun find(searchType: String) {
        Console.MatchInput.show()
        val search = readln().lowercase().split(" ")
        var print = mutableSetOf<String>()

        when (searchType) {
            "any" -> {
                search.forEach { word ->
                    if (words.containsKey(word)) {
                        words.getValue(word).forEach { i ->
                            print.add(people[i]) }
                    }
                }
            }
            "all" -> {
                search.forEach { word ->
                    if (words.containsKey(word)) {
                        val line = words.filterValues { it == words.getValue(word) }.keys
                        if (line.containsAll(search)) {
                            print.add(line.joinToString(" "))
                        }
                    }
                }
            }
            "none" -> {
                print = people.toMutableSet()
                for (line in people) {
                    val list = line.split(" ")
                    for (str in list) { if (str.lowercase() in search) { print.remove(line) } }
                }
            }
        }

        val plural = if (print.size > 1) "s" else ""
        Console.Found.show(print.size.toString(), plural)
        print.forEach(::println)
    }

    private fun peoplePrint() {
        Console.Print.show()
        println(people.joinToString("\n"))
    }
}

fun main(args: Array<String>) { Search(args) }