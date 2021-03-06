package com.cshep4.premierpredictor.matchupdate.component.api

import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpGet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component(value = "footballApi")
class ApiRequester : DataRetriever {
    companion object {
        const val FROM_DATE: String = "2020-09-01"
        const val TO_DATE: String = "2021-06-01"
        const val COMP_ID: String = "1204"
    }

    @Value("\${API_URL}")
    private val apiUrl = ""

    @Value("\${API_KEY}")
    private val apiKey = ""

    @Value("\${API_COMMENTARY_URL}")
    private val apiCommentaryUrl = ""

    override fun retrieveFixtures(): List<MatchFacts> {
        val url = "$apiUrl?from_date=$FROM_DATE&to_date=$TO_DATE&comp_id=$COMP_ID&Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Array<MatchFacts>::class.java).toList().map { it.toSantisedMatchFacts() }
        }, {
            return@fold emptyList()
        })
    }

    override fun retrieveCommentary(id: String): Commentary? {
        val url = "$apiCommentaryUrl$id?Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Commentary::class.java)
        }, {
            return@fold null
        })
    }

    override fun retrieveMatch(id: String): MatchFacts? {
        val url = "$apiUrl$id?Authorization=$apiKey"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, MatchFacts::class.java)
        }, {
            return@fold null
        })
    }

}