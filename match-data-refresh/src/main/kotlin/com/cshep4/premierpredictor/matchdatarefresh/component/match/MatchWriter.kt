package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.repository.FixtureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchWriter {
    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    fun update(matches: List<Match>): List<Match> {
        if (matches.isEmpty()) {
            return emptyList()
        }

        fixtureRepository.save(matches)

        return matches
    }
}