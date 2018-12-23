package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.OverrideMatch
import org.springframework.stereotype.Component

@Component
class MatchOverrider {
    fun update(matches: List<Match>, overrides: List<OverrideMatch>) : List<Match> {
        matches.forEach {
            val id = it.id
            val override = overrides.firstOrNull { o -> o.id == id } ?: return@forEach

            it.hGoals = override.hGoals
            it.aGoals = override.aGoals
        }

        return matches
    }
}