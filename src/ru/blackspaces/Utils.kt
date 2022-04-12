package ru.blackspaces.ru.blackspaces

import java.util.*
import kotlin.streams.asSequence

object Utils {
    fun randomString(length: Long): String {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        return Random().ints(length, 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
    }
}