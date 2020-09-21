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

    fun retrieveLatest(id: String): MatchFacts? {
        val storedMatch = liveMatchServiceRepository
                .findById(id)

        val updatedMatch = matchFactsRetriever.getLatest(id)

        val matchFacts = updatedMatch ?: storedMatch ?: return null

        if (updatedMatch?.commentary == null) {
            updatedMatch.commentary = storedMatch?.commentary
        }

        liveMatchServiceRepository.save(matchFacts)

        return matchFacts
    }
}