package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MatchUpdater {
    @Autowired
    private lateinit var matchWriter: MatchWriter

    fun update(matchFacts: List<MatchFacts>): List<Match> {
        if (matchFacts.isEmpty()) {
            return emptyList()
        }

        val matches = matchFacts.map { it.toMatch() }

        return matchWriter.update(matches)
    }
}
