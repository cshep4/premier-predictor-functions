package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchEntity
import com.cshep4.premierpredictor.matchdatarefresh.repository.sql.FixturesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchWriter {
    @Autowired
    private lateinit var fixturesRepository: FixturesRepository

    fun update(matches: List<Match>): List<Match> {
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        if (matchEntities.isEmpty()) {
            return emptyList()
        }

        return fixturesRepository.saveAll(matchEntities).map { it.toDto() }
    }
}