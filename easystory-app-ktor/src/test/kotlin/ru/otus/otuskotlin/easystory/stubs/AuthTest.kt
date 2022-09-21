package ru.otus.otuskotlin.easystory.stubs

import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.easystory.config.AuthConfig
import ru.otus.otuskotlin.easystory.helpers.addAuth
import ru.otus.otuskotlin.easystory.module
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application { module(authConfig = AuthConfig.TEST) }

        val response = client.post("/block/create") {
            addAuth(config = AuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
