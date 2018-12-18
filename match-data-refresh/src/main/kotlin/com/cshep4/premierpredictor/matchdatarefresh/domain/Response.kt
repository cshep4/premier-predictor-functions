package com.cshep4.premierpredictor.matchdatarefresh.domain

data class Response(val statusCode: Int = 500,
                    val headers: Map<String, String> = emptyMap(),
                    val body: String? = null)