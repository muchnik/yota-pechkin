package com.yota.pechkin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.lang.System.currentTimeMillis

@SpringBootApplication
class PechkinApplication

fun main(args: Array<String>) {
    runApplication<PechkinApplication>(*args)
}

data class YotaMessage(
        val token: String?,
        val from: User,
        val to: User,
        val message: String
)

data class Response(val reason: String)

data class User(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String
)

@RestController
class PechkinController {
    @PostMapping("/api/v1/dostavit-bistro")
    fun receive(@RequestBody message: YotaMessage, @RequestHeader("x-info") xInfo: String): ResponseEntity<*> = when {
        message.token.isNullOrBlank() -> status(HttpStatus.BAD_REQUEST).body(Response("Требуется особо секретный token"))
        currentTimeMillis() % 2 == 0L -> status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response("Извини, устал педали крутить..."))
        else                          -> ok(Response("Доставим мы посылочку вашему мальчику ${message.to.firstName}, не волнуйтесь!"))
    }
}