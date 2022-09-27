package ru.otus.otuskotlin.easystory.helpers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.*
import io.ktor.http.*
import ru.otus.otuskotlin.easystory.config.AuthConfig

fun HttpRequestBuilder.addAuth(
    id: String = "user1",
    groups: List<String> = listOf("USER"),
    config: AuthConfig = AuthConfig.TEST
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(AuthConfig.GROUPS_CLAIM, groups)
        .withClaim(AuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    header(HttpHeaders.Authorization, "Bearer $token")
}
