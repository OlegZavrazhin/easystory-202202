package ru.otus.otuskotlin.easystory.config

import io.ktor.server.engine.*

data class AuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
) {
    // FIXME: exclude to a separate extension
    constructor(environment: ApplicationEngineEnvironment): this(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )

    companion object {
        const val ID_CLAIM = "id"
        const val GROUPS_CLAIM = "groups"

        val TEST = AuthConfig(
            secret = "secret",
            issuer = "es",
            audience = "es-users",
            realm = "Access to Blocks"
        )
    }
}
