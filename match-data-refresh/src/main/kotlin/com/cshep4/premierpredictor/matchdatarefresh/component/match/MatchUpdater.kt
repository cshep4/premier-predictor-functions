package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.component.api.ApiRequester
import com.cshep4.premierpredictor.matchdatarefresh.component.time.Time
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchdatarefresh.extensions.isInFuture
import com.cshep4.premierpredictor.matchdatarefresh.repository.MatchFactsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchUpdater {
    @Autowired
    private lateinit var apiRequester: ApiRequester

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var time: Time

    fun matchData(): List<MatchFacts> {
        val apiResult = apiRequester.retrieveFixtures()

        val updated = matchFactsRepository.findAll()
                .filter { it.getDateTime()!!.isInFuture() }
                .map { mergeWithLatestVersion(it.toDto(), apiResult.firstOrNull { m -> it.id == m.id }) }

        val updatedMatchEntities = updated.map { MatchFactsEntity.fromDto(it) }

        matchFactsRepository.saveAll(updatedMatchEntities)

        return updated
    }

    private fun mergeWithLatestVersion(match: MatchFacts, apiMatch: MatchFacts?): MatchFacts {
        apiMatch ?: return match

        apiMatch.commentary = match.commentary
        apiMatch.lastUpdated = time.localDateTimeNow()

        return apiMatch
    }
}
