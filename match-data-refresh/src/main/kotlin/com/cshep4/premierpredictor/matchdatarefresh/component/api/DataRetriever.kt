package com.cshep4.premierpredictor.matchdatarefresh.component.api

import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts

interface DataRetriever {
    fun retrieveFixtures(): List<MatchFacts>
    fun retrieveCommentary(id: String): Commentary?
    fun retrieveMatch(id: String): MatchFacts?
}