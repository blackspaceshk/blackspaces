package ru.blackspaces.clients


interface SnClient {
    fun token(): String
    fun posts(): Iterable<CommonSnPost>
    fun userId(): String
}