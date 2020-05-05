package com.cshep4.premierpredictor.matchdatarefresh.component.matchfacts

import com.cshep4.premierpredictor.matchdatarefresh.component.time.Time
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.cshep4.premierpredictor.matchdatarefresh.extensions.isInFuture
import com.cshep4.premierpredictor.matchdatarefresh.extensions.isYesterday
import com.cshep4.premierpredictor.matchdatarefresh.repository.LiveMatchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchFactsUpdater {
    @Autowired
    private lateinit var liveMatchRepository: LiveMatchRepository

    @Autowired
    private lateinit var time: Time

    fun update(apiResult: List<MatchFacts>): List<MatchFacts> {
        val updated = liveMatchRepository.findAll()
                .filter { it.wasRescheduled() || it.getDateTime()!!.isInFuture() || it.getDateTime()!!.isYesterday() }
                .map {
                    mergeWithLatestVersion(
                            it,
                            apiResult.firstOrNull { m -> it.id == m.id }
                    )
                }

        liveMatchRepository.save(updated)

        return updated
    }

    private fun mergeWithLatestVersion(match: MatchFacts, apiMatch: MatchFacts?): MatchFacts {
        apiMatch ?: return match

        apiMatch.commentary = match.commentary
        apiMatch.lastUpdated = time.localDateTimeNow()

        return apiMatch
    }
}
