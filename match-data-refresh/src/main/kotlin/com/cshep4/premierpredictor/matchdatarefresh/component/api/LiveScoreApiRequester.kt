package com.cshep4.premierpredictor.matchdatarefresh.component.api

import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpGet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component(value = "liveScore")
class LiveScoreApiRequester: DataRetriever {
    companion object {
        const val FROM_DATE: String = "2020-09-01"
        const val TO_DATE: String = "2021-06-01"
        const val COMP_ID: String = "EPL"
    }

    @Value("\${API_URL}")
    private val apiUrl = ""

    @Value("\${API_COMMENTARY_URL}")
    private val apiCommentaryUrl = ""

    override fun retrieveFixtures(): List<MatchFacts> {
        val url = "$apiUrl$COMP_ID/events?fromDate=$FROM_DATE&toDate=$TO_DATE"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Array<MatchFacts>::class.java).toList().map { it.toSantisedMatchFacts() }
        }, {
            return@fold emptyList()
        })
    }

    override fun retrieveCommentary(id: String): Commentary? {
        val url = "$apiCommentaryUrl$id"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, Commentary::class.java)
        }, {
            return@fold null
        })
    }

    override fun retrieveMatch(id: String): MatchFacts? {
        val url = "$apiUrl$id"
        val (_, _, result) = url.httpGet().responseString()

        return result.fold({ data ->
            return@fold ObjectMapper().readValue(data, MatchFacts::class.java)
        }, {
            return@fold null
        })
    }

}