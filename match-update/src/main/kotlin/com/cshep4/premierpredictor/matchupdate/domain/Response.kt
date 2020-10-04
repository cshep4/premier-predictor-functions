package com.cshep4.premierpredictor.matchupdate.domain

//data class Response(val statusCode: Int = 500,
//                    val headers: Map<String, String> = emptyMap(),
//                    val body: String? = null)

enum class State {
    UPDATE_USER_SCORES,
    WAIT,
    END
}

data class Response(val next: State)
