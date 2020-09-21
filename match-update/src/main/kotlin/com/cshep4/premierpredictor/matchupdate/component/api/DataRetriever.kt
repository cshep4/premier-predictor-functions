package com.cshep4.premierpredictor.matchupdate.component.api

import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts


interface DataRetriever {
    fun retrieveFixtures(): List<MatchFacts>
    fun retrieveCommentary(id: String): Commentary?
    fun retrieveMatch(id: String): MatchFacts?
}