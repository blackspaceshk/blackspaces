package ru.blackspaces.ru.blackspaces

class TextChecker(private val dictionary: List<String>) {
    fun isBad(text: String): Boolean {
        return !dictionary.none{ text.contains(it) }
    }
}