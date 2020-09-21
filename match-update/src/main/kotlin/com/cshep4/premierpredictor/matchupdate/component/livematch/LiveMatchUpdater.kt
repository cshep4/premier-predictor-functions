package com.cshep4.premierpredictor.matchupdate.component.livematch

import com.cshep4.premierpredictor.matchupdate.component.update.CommentaryRetriever
import com.cshep4.premierpredictor.matchupdate.component.update.MatchFactsRetriever
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LiveMatchUpdater {
    @Autowired
    private lateinit var liveMatchServiceRepository: LiveMatchServiceRepository

    @Autowired
    private lateinit var matchFactsRetriever: MatchFactsRetriever

    @Autowired
    private lateinit var commentaryRetriever: CommentaryRetriever

    fun retrieveLatest(id: String): MatchFacts? {
        val storedMatch = liveMatchServiceRepository
                .findById(id)

        val matchFacts = matchFactsRetriever.getLatest(id)

        return updateMatchFacts(storedMatch, matchFacts)
    }

    private fun updateMatchFacts(storedMatch: MatchFacts?, updatedMatch: MatchFacts?): MatchFacts? {
        val matchFacts = updatedMatch ?: storedMatch ?: return null

        if (matchFacts.commentary == null) {
            matchFacts.commentary = storedMatch?.commentary
        }

        liveMatchServiceRepository.save(matchFacts)

        return matchFacts
    }
}