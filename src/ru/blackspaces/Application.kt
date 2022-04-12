package ru.blackspaces.ru.blackspaces

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import io.ktor.routing.*
import org.slf4j.event.*
import io.ktor.http.content.*
import com.fasterxml.jackson.databind.*
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource
import io.ktor.http.*
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.blackspaces.clients.FacebookClient
import ru.blackspaces.config.SocialNetworksConfig
import ru.blackspaces.runner.CheckRunner
import ru.blackspaces.text.FuzzyWuzzyTextChecker
import java.net.URI
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Routing)
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ConditionalHeaders)
    install(CachingHeaders)
    install(DataConversion)
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    Database.connect("jdbc:h2:mem:social-helper;DB_CLOSE_DELAY=-1\n", driver = "org.h2.Driver")
    transaction {
        addLogger(Slf4jSqlDebugLogger)

        SchemaUtils.create(Tasks, Posts)
    }
    val textChecker = TextChecker(listOf("Информатика и Теорвер", "http://code.google.com/p/mipt-home/"))

    val socialNetworkConfig = ConfigLoader.Builder()
        .addSource(PropertySource.resource("/facebook.yaml", optional = true))
        .build()
        .loadConfigOrThrow<SocialNetworksConfig>()
    val checkRunner = CheckRunner(FuzzyWuzzyTextChecker(emptySet()))

    routing {
        static("/static") {
            resources("static")
            defaultResource("static/index.html")
        }

        get("/") {
            call.respondRedirect("/static/index.html")
        }

        post("/check") {
            val request = call.receive<CheckRequest>()
            val facebookClient = FacebookClient(request.token)
            val task = checkRunner.check(facebookClient)
            call.respond(task)
        }

        get("/detected/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val result = checkRunner.resultForId(id)
            if (result == null) {
                call.respond(HttpStatusCode.NotFound, "No task with such id")
            } else {
                call.respond(result)
            }
        }
    }
}

data class CheckRequest(val tokenType: String, val token: String)
data class DetectedLocation(val id: UUID)