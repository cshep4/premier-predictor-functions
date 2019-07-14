package com.cshep4.premierpredictor.matchdatarefresh.component.api

import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpGet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApiRequester {
    companion object {
        const val FROM_DATE: String = "2019-08-08"
        const val TO_DATE: String = "2020-05-18"
        const val COMP_ID: String = "1204"
    }

    @Value("\${API_URL}")
    private val apiUrl = ""

    @Value("\${API_KEY}")
    private val apiKey = ""

    @Value("\${API_COMMENTARY_URL}")
    private val apiCommentaryUrl = ""

    fun retrieveFixtures(): List<MatchFacts> {
        val url = "$apiUrl?from_date=$FROM_DATE&to_date=$TO_DATE&comp_id=$COMP_ID&Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Array<MatchFacts>::class.java).toList().map { it.toSantisedMatchFacts() }
        }, {
            return@fold emptyList()
        })
    }

    fun retrieveCommentary(id: String): Commentary? {
        val url = "$apiCommentaryUrl$id?Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Commentary::class.java)
        }, {
            return@fold null
        })
    }

    fun retrieveMatch(id: String): MatchFacts? {
        val url = "$apiUrl$id?Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, MatchFacts::class.java)
        }, {
            return@fold null
        })
    }

}