package ru.otus.otuskotlin.easystory

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.easystory.app.v1Block
import ru.otus.otuskotlin.easystory.common.models.ESSettings
import ru.otus.otuskotlin.easystory.config.AuthConfig
import ru.otus.otuskotlin.easystory.config.AuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.easystory.mappers.toModel
import ru.otus.otuskotlin.easystory.repository.inmemory.BlockRepoInMemory
import ru.otus.otuskotlin.easystory.repository.postgresql.RepoBlockSQL
import ru.otus.otuskotlin.easystory.services.BlockService

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.getAuthConfig(): AuthConfig = AuthConfig(
    secret = environment.config.property("jwt.secret").getString(),
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString()
)

//@Suppress("unused") // Referenced in application.conf
fun Application.module(
    settings: ESSettings? = null,
    authConfig: AuthConfig = getAuthConfig()
) {
    install(Routing)

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }


    install(CallLogging) {
        level = Level.INFO
    }

    install(Locations)

    val corSettings by lazy {
        settings ?: ESSettings(
            repoTest = BlockRepoInMemory(),
            repoProd = RepoBlockSQL(url = "jdbc:postgresql://localhost:5432/esdb")
        )
    }

    val blockService = BlockService(corSettings)

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            )
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Healthcheck!")
        }

        authenticate("auth-jwt") {
            v1Block(blockService) { principal<JWTPrincipal>().toModel() }
        }

        static("static") {
            resources("static")
        }
    }
}
