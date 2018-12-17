package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.FixturesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchWriter {
    @Autowired
    private lateinit var fixturesRepository: FixturesRepository

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    fun matches(matches: Collection<Match>): List<Match> {
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        return fixturesRepository.saveAll(matchEntities)
                .map { it.toDto() }
    }

    fun matchFacts(matchFacts: Collection<MatchFacts>): List<MatchFacts> {
        val matchFactsEntities = matchFacts.map { MatchFactsEntity.fromDto(it) }

        return matchFactsRepository.saveAll(matchFactsEntities)
                .map { it.toDto() }
    }
}