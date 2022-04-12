package ru.blackspaces.runner

import ru.blackspaces.text.CheckFoundResult
import java.util.*

data class CheckTask(val id: UUID, var processedCount: Int = 0, var results: Collection<PostCheckResult>? = null)
