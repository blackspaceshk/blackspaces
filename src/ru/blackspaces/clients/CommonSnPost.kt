package ru.blackspaces.clients

import java.time.Instant

data class CommonSnPost(
    val message: String,
    val link: String,
    val createdDate: Instant,
    val friendsOnly: Boolean
)