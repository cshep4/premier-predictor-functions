package com.cshep4.premierpredictor.matchupdate.component.leaguetable

import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.data.LeagueTable
import com.cshep4.premierpredictor.matchupdate.data.Match
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LeagueTableCollector {
    @Autowired
    private lateinit var matchReader: MatchReader

    @Autowired
    private lateinit var leagueTableCalculator: LeagueTableCalculator

    fun getCurrentLeagueTable() : LeagueTable {
        val matches = matchReader.retrieveAllMatches()

        return createLeagueTableFromMatches(matches)
    }

    fun getPredictedLeagueTable(id: Long): LeagueTable {
        val matches = matchReader.retrieveAllMatchesWithPredictions(id)
                .map { it.toMatch() }

        return createLeagueTableFromMatches(matches)
    }

    fun createLeagueTableFromMatches(matches: List<Match>) : LeagueTable {
        val leagueTable = LeagueTable.emptyTable()

        return leagueTableCalculator.calculate(matches, leagueTable)
    }
}