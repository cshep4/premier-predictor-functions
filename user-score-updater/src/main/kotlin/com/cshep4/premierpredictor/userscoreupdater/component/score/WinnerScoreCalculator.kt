package com.cshep4.premierpredictor.userscoreupdater.component.score

import com.cshep4.premierpredictor.userscoreupdater.component.leaguetable.LeagueTableCollector
import com.cshep4.premierpredictor.userscoreupdater.constant.MatchConstants.NUM_MATCHES
import com.cshep4.premierpredictor.userscoreupdater.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class WinnerScoreCalculator {
    companion object {
        const val WINNER_ADDITION = 20
    }

    @Autowired
    private lateinit var leagueTableCollector: LeagueTableCollector

    fun calculate(users: List<User>): List<User> {
        val leagueTable = leagueTableCollector.getCurrentLeagueTable()

        if (leagueTable.table.any { it.played < NUM_MATCHES }) {
            return users
        }

        val winner = leagueTable.table[0].teamName

        users.filter { it.predictedWinner == winner }
                .forEach { it.score += WINNER_ADDITION }

        return users
    }
}