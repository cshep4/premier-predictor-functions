package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchWriter {
    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    @Autowired
    private lateinit var liveMatchServiceRepository: LiveMatchServiceRepository

    fun fixtures(fixtures: Collection<Match>): List<Match> {
        val fixtureList = fixtures.toList()

        fixtureRepository.save(fixtureList)

        return fixtureList
    }

    fun matchFacts(matchFacts: Collection<MatchFacts>): List<MatchFacts> {
        val matches = matchFacts.toList()

        liveMatchServiceRepository.save(matches)

        return matches
    }
}