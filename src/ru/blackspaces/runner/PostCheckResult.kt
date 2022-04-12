package ru.blackspaces.runner

import ru.blackspaces.clients.CommonSnPost
import ru.blackspaces.text.CheckFoundResult

data class PostCheckResult(val post: CommonSnPost, val foundResult: Collection<CheckFoundResult>)