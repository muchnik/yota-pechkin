package com.yota.pechkin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.lang.System.currentTimeMillis
import java.util.*

@SpringBootApplication
class PechkinApplication

fun main(args: Array<String>) {
    runApplication<PechkinApplication>(*args)
}

data class YotaMessage(
        val token: String?,
        val messageId: Long,
        val from: User,
        val to: User,
        val message: String,
        val addresses: List<Address>
)

data class Address(val id: Long, val street: String, val houseColor: String)

data class Response(val message: String)

data class Token(val token: String)

data class User(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String
)

@RestController
@RequestMapping("/api/v1/")
class PechkinController {
    @PostMapping("/deliver")
    fun receive(@RequestBody message: YotaMessage): ResponseEntity<*> = when {
        message.token.isNullOrBlank()     -> status(HttpStatus.BAD_REQUEST).body(Response("Требуется особо секретный token, можно получить с помощью запроса GET /api/v1/token"))
        message.addresses.isNullOrEmpty() -> status(HttpStatus.BAD_REQUEST).body(Response("Куда же я доставлять посылочку буду? Где адреса?"))
        currentTimeMillis() % 2 == 0L     -> status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response("Извини, устал педали крутить..."))
        else                              -> ok(Response("Доставим мы посылочку вашему мальчику ${message.to.firstName}, не волнуйтесь!"))
    }

    @GetMapping("/token")
    fun token() = Token(UUID.randomUUID().toString())
}