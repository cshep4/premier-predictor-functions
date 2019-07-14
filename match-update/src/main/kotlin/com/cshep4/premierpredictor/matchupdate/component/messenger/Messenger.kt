package com.cshep4.premierpredictor.matchupdate.component.messenger

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpPost
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Messenger {
    @Value("\${MESSENGER_CLIENT_URL}")
    private val messengerUrl = ""

    fun send(message: String) {
        val body = mapOf(Pair("text", message))
        val json = ObjectMapper().writeValueAsString(body)

        val request = messengerUrl.httpPost().body(json)
        request.headers["Content-Type"] = "application/json"

        val (_, _, _) = request.responseString()
    }
}