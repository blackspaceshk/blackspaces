package ru.blackspaces.text


interface TextChecker {
    fun checkText(text: String): Collection<CheckFoundResult>
}