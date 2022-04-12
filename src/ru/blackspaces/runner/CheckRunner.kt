package ru.blackspaces.runner

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.blackspaces.clients.SnClient
import ru.blackspaces.text.TextChecker
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CheckRunner(private val textChecker: TextChecker) {
    private val tasks = ConcurrentHashMap<UUID, CheckTask>()

    fun check(client: SnClient): UUID {
        val taskId = UUID.randomUUID()
        val task = CheckTask(taskId)
        tasks[taskId] = task
        CoroutineScope(Dispatchers.Default)
            .launch(Dispatchers.Default) {
                val checkResult = client.posts()
                    .onEach { task.processedCount++ }
                    .map { PostCheckResult(it, textChecker.checkText(it.message)) }
                task.results = checkResult
            }

        return taskId
    }

    fun resultForId(id: UUID): CheckTask? {
        val result = tasks[id]
        if (result?.results != null) {
            tasks.remove(id)
        }

        return result
    }
}