package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MatchUpdater {
    @Autowired
    private lateinit var overrideMatchRetriever: OverrideMatchRetriever

    @Autowired
    private lateinit var matchOverrider: MatchOverrider

    @Autowired
    private lateinit var matchWriter: MatchWriter

    fun update(matchFacts: List<MatchFacts>): List<Match> {
        if (matchFacts.isEmpty()) {
            return emptyList()
        }

        val matches = matchFacts.map { it.toMatch() }

        val overrides = overrideMatchRetriever.findAll()

        val overriddenMatches = matchOverrider.update(matches, overrides)

        return matchWriter.update(overriddenMatches)
    }
}
