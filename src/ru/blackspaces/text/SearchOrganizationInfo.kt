package ru.blackspaces.text

import java.net.URI
import java.time.LocalDate

data class SearchOrganizationInfo(
    val documentId: Int,
    val name: String,
    val addedDate: LocalDate,
    val links: Collection<URI>,
    val synonyms: Collection<String>
)
